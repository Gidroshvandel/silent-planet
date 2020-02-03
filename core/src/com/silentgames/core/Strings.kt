package com.silentgames.core

enum class Strings(private var string: String) {

    aliens("Инопланетяне"),
    pirates("Пираты"),
    humans("Земляне"),
    robots("Роботы"),

    crystal_count("%s\n%d/%d"),

    dead_player("Мёртв"),
    dead_player_description("Ещё один труп на холодной планете"),

    unknown_cell_name("Unknown"),
    unknown_cell_description("Terra Incognita"),

    space_cell_name("Космос"),
    space_cell_description("Участок неизведанного космоса"),

    empty_cell_name("Пустошь"),
    empty_cell_description("Пустынная поверхность планеты"),

    death_cell_name("Смерть"),
    death_cell_description("Мгновенная смерть по неизвестным причинам"),

    crystal_cell_name("Кристалы"),
    crystal_cell_description("Кристалов %d"),

    tornado_cell_name("Ледяной смерчь"),
    tornado_cell_description("Ледяной смерчь переносит на ближайщий угол"),

    abyss_cell_name("Пропасть"),
    abyss_cell_description("Пропасть возвращает на ближайщую карточку без стрелки"),

    arrow_cell_name("Стрела"),

    arrow_green_cell_description("Зелёная стрела перемещает на 1 клетку"),
    arrow_red_cell_description("Красная стрела перемещает на 2 клетки"),

    robot_player_name_one("Х-3002"),
    robot_player_name_two("Z-702"),
    robot_player_name_three("P-02"),
    robot_player_description("Автоматическая единица - робот"),
    pirate_player_name_one("Марок"),
    pirate_player_name_two("Пинес"),
    pirate_player_name_three("Андре"),
    pirate_player_description("Корсар из экипажа пиратов"),
    human_player_name_one("Мирослав"),
    human_player_name_two("Максим"),
    human_player_name_three("Оксана"),
    human_player_description("Космонавт с корабля землян"),
    alien_player_name_one("Илио"),
    alien_player_name_two("Нараам"),
    alien_player_name_three("Пахар"),
    alien_player_description("Представитель загадочной рассы"),

    robot_ship_name("Зиро"),
    robot_ship_description("Космический корабль роботов"),
    pirate_ship_name("Корсар"),
    pirate_ship_description("Космический корабль пиратов"),
    human_ship_name("Пионер"),
    human_ship_description("Космический корабль землян"),
    alien_ship_name("Лиира"),
    alien_ship_description("Космический корабль загадочной рассы"),

    get_crystal_action("Взять кристал"),

    player_buyback_success("%s возвращён на корабль"),
    player_buyback_failure("Необходимо ещё кристалов: %1d"),
    captive("Пленный"),
    buyout("Выкуп");

    fun getString(vararg args: Any?): String = String.format(string, *args)

}