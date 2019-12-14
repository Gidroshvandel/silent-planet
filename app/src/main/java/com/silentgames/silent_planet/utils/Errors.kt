package com.silentgames.silent_planet.utils

//Если не найден корабль то что-то не так(должен быть всегда)
class ShipNotFoundException : NullPointerException("Ship didn't find, SPAWN any Ship")