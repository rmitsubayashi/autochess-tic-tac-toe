package com.github.rmitsubayashi.action

import com.github.rmitsubayashi.game.Game
import java.util.*

class ActionObservable(private val game: Game) {
    private val eventQueue = LinkedList<Event>()
    private val actionList = mutableListOf<Action>()
    lateinit var currEvent: Event
    private val tempActionQueue = PriorityQueue<Action>()


    fun subscribeAction(action: Action) {
        actionList.add(action)
    }

    fun subscribeActions(actions: List<Action>) {
        actionList.addAll(actions)
    }

    fun unsubscribeActions(actions: List<Action>) {
        actionList.removeAll(actions)
    }

    fun notifyAllActions(event: Event) {
        eventQueue.add(event)
        consumeQueueEvents()
    }

    fun notifyAllActions(events: List<Event>) {
        eventQueue.addAll(events)
        consumeQueueEvents()
    }

    private fun consumeQueueEvents() {
        while (eventQueue.isNotEmpty()) {
            currEvent = eventQueue.pop()
            for (action in actionList) {
                if (action.conditionMet(game, currEvent)) {
                    tempActionQueue.add(action)
                }
            }
            while (tempActionQueue.isNotEmpty()) {
                val action = tempActionQueue.remove()
                val newEvents = action.execute(game, currEvent)
                eventQueue.addAll(newEvents)
            }
        }
    }
}