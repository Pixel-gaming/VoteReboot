package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.pluginapi.Scoreboard.*;
import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.votereboot.config.*;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Permission;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;
public class VoteAction extends RestartAction {

    @Getter
    private HashMap<String, Optional<Boolean>> votes = new HashMap<>();
    private AtomicLong no = new AtomicLong();
    private AtomicLong yes = new AtomicLong();
    private final Scoreboard scoreboard = Scoreboard.getNew();
    private final Objective sidebarObjective = Objective.createNew(scoreboard,"reboot",AnnounceConfig.getInstance().getScoreboardTitle().getValue(), Criteria.Dummy);
    private final Score yesScore = sidebarObjective.getOrCreateScore("yes");
    private final Score noScore = sidebarObjective.getOrCreateScore("no");
    private final Score timeScore = sidebarObjective.getOrCreateScore("time");
    public VoteAction() {
        super(RestartType.Vote, new TimeUnitValue(TimeUnit.SECONDS,VoteConfig.getInstance().getVotingTime().getValue()));
        //then the scoreboard
        try{
            //if this add fails, the objective was already a part of the scoreboard
            scoreboard.addObjective(sidebarObjective);
        }catch (IllegalArgumentException ignored){}
        //this should never return an IllegalStateException, because we make sure above that the objective is registered.
        scoreboard.updateDisplaySlot(sidebarObjective,DisplaySlot.Sidebar);
    }

    @Override
    protected void intStart(boolean checkAnnounce){
        showScoreboard();
        super.intStart(checkAnnounce);
    }

    public boolean addVote(@NonNull Permission perm, @NonNull String src, @NonNull Optional<Boolean> vote) {
        if (perm.hasPerm(ConfigPermission.getInstance().getVoteRegister().getValue())){
            getLogger().info("{} voted {}",src,vote.isPresent()?vote.get():"none");
            votes.put(src, vote);
            return true;
        }
        return false;
    }

    @Override
    protected void doReset() {
        votes = new HashMap<>();
        yes=new AtomicLong(0);
        no=new AtomicLong(0);
        super.doReset();
    }

    @Override
    protected void scoreboard(){
        //this causes too much console spam. Don't do it this way.
        if (AnnounceConfig.getInstance().getEnableScoreboard().getValue()){
            API.runOnMain(()->{
                updateVotes();
                yesScore.setScore((int) yes.get());
                noScore.setScore((int) no.get());
                timeScore.setScore((int) TimeUnit.SECONDS.convert(timer.get(),timerUnit.get()));
            });
//            API.getBuilder().executer(()->{
//                getLogger().info("Updating Scoreboard");
//                API.getServer().execCommand("say test");
//                API.getServer().execCommand("minecraft:scoreboard objectives remove restart");
//                API.getServer().execCommand("minecraft:scoreboard objectives add restart dummy "+timer.get()+" "+timerUnit.get().toString().toLowerCase());
//                API.getServer().execCommand("minecraft:scoreboard objectives setdisplay sidebar restart");
//                API.getServer().execCommand("minecraft:scoreboard players set yes restart "+yes.get());
//                API.getServer().execCommand("minecraft:scoreboard players set no restart "+no.get());
//            }).name("votereboot-S-updateScoreboard").build();
        }
    }
    protected void hideScoreboard(){
        if(AnnounceConfig.getInstance().getEnableScoreboard().getValue())
            API.runOnMain(()->{
                scoreboard.clearSlot(DisplaySlot.Sidebar);
                for(val world:API.getServer().getWorlds()){
                    for(val player:world.getPlayers()){
                        player.getScoreboard().clearSlot(DisplaySlot.Sidebar);
                    }
                }
            });
    }
    protected void showScoreboard(){
        if(AnnounceConfig.getInstance().getEnableScoreboard().getValue())
            API.runOnMain(()->{
                sidebarObjective.setDisplayName(AnnounceConfig.getInstance().getScoreboardTitle().getValue());
                yesScore.setScore(0);
                noScore.setScore(0);
                scoreboard.updateDisplaySlot(sidebarObjective,DisplaySlot.Sidebar);
                for(val world:API.getServer().getWorlds()){
                    for(val player:world.getPlayers()){
                        player.setScoreboard(scoreboard);
                    }
                }
            });
    }
    @Override
    protected boolean cancelTimer(boolean del){
        hideScoreboard();
        return super.cancelTimer(del);
    }
    private void updateVotes(){
        yes.set(0);
        no.set(0);
        votes.values().stream().filter(Optional::isPresent).map(Optional::get)
                .forEach(v->{if(v) yes.incrementAndGet();else no.incrementAndGet();});
    }

    @Override
    protected void timerDone() {
        if (restartType==RestartType.Vote){
            updateVotes();
            double percent = (yes.get() / 1.0 / (yes.get() + no.get())) * 100.0;
            getLogger().info("{} yes, {} no, {}% of the people that voted want the server to be restarted", yes.get(),no.get(),percent);
            getLogger().info("Votes: {}",votes.values().stream().map(e-> e.map(aBoolean -> aBoolean ? "true" : "false").orElse("none")).collect(Collectors.toList()));
            getLogger().info("Voters: {}",votes.keySet());
            if (yes.get() >= VoteConfig.getInstance().getMinAgree().getValue() &&
                    percent >= VoteConfig.getInstance().getPercentToRestart().getValue()) {
                restartType=RestartType.All;
                long votingRestartTime = VoteConfig.getInstance().getVotingRestartTime().getValue();
                timer.set(votingRestartTime);
                API.getServer().sendMessage(ConfigStrings.getInstance().getVoteRestartSuccess().getValue().replaceFirst("\\{\\}",Long.toString(votingRestartTime)).replaceFirst("\\{\\}","s"));
                hideScoreboard();
            }else {
                API.getServer().sendMessage(ConfigStrings.getInstance().getVoteRestartFailed().getValue());
                hideScoreboard();
                cancelTimer(true);
                doReset();
            }
        }else{
            //The timer was done once, and the vote was successful
            super.timerDone();
        }
    }

    public boolean isVoteInProgress() {
        return task.isPresent() && timer.get()>=0;
    }


}
