package com.trenbologna.stronk.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CustomPair<T, E> {
    private T before;
    private E after;
    public static <T, E> CustomPair<T, E> of(final T before, final E after) {
        return new CustomPair<>(before, after);
    }
}
