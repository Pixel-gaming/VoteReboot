package com.c0d3m4513r.pluginapi;

public interface Task {
    /**
     * Gets the name of this task.
     *
     * @return The name of the task
     */
    String getName();

    /**
     * Gets the delay that the task was scheduled to run after. A delay of 0
     * represents that the task started immediately.
     *
     * @return The delay (offset) in either milliseconds or ticks (ticks are
     *         exclusive to synchronous tasks)
     */
    long getDelay();

    /**
     * Gets the interval for repeating tasks. An interval of 0 represents that
     * the task does not repeat.
     *
     * @return The interval (period) in either milliseconds or ticks (ticks are
     *         exclusive to synchronous tasks)
     */
    long getInterval();

    /**
     * Gets whether this task is asynchronous.
     *
     * @return True if asynchronous, false if synchronous
     */
    boolean isAsynchronous();
    /**
     * Cancels the task. Cancelling a repeating task will prevent any further
     * repetitions of the task. This MUST always be the case, and it is the implementations job to make sure.
     *
     * @return If the task is not running and was cancelled
     */
    boolean cancel();
}
