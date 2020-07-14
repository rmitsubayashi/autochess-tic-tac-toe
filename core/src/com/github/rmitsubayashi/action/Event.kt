package com.github.rmitsubayashi.action

data class Event(
        val type: EventType,
        val actor: EventActor?,
        val actedUpon: EventActor?,
        val data: Map<EventDataKey, Any>? = null
)