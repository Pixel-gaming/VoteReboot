package com.c0d3m4513r.pluginapi.command;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class CommandException extends Throwable{
    @NonNull
    String message;
    Throwable e;
}
