package com.github.rmitsubayashi.entity

enum class Race(val stringVal: String) {
    SLIME("slime"),
    FALLEN("fallen");
    //BEAST("beast"),
    //UNDEAD("undead");

    companion object {
        fun fromString(str: String): Race {
            for (race in values()) {
                if (race.stringVal == str) return race
            }
            throw ClassNotFoundException()
        }
    }
}