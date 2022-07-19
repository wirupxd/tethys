package me.alpha432.oyvey.event.processor;

public class FaggotEvent {
    private boolean isCancelled;

    public FaggotEvent(){
        isCancelled = false;
    }

    public final boolean isCancelled() {
        return isCancelled;
    }

    /**
     * @param cancelled boolean to set if the event is cancelled
     */
    public final void setCancelled(final boolean cancelled) {
        isCancelled = cancelled;
    }
}
