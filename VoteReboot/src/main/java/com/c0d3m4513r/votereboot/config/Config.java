package com.c0d3m4513r.votereboot.config;


import com.c0d3m4513r.voterebootapi.config.MainConfig;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class Config extends MainConfig implements IConfigLoadableSaveable {
    private VoteConfig voteConfig = null;

    @Override
    public void loadValue() {
        voteConfig.loadValue();
    }

    @Override
    public void saveValue() {
        voteConfig.saveValue();
    }
}
