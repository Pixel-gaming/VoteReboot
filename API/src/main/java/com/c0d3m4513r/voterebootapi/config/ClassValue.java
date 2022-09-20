package com.c0d3m4513r.voterebootapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassValue<T> {
    T value;
    Class<T> clazz;
}
