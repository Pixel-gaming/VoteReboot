package com.c0d3m4513r.voterebootapi.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommandException extends Throwable{
    String message;
    Throwable e;
}
