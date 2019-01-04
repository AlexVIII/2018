package ru.job4j.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * StartUITest - test class.
 *
 *@author John Ivanov (johnivo@mail.ru)
 *@version $Id$
 *@since 02.01.2019
 */

public class StartUITest {

    //Константа для меню.
    private static final String MENU = new StringBuilder()
            .append("Меню.").append(System.lineSeparator())
            .append("1. Вывести список всех заявок").append(System.lineSeparator())
            .append("2. Редактировать заявку").append(System.lineSeparator())
            .append("3. Удалить заявку").append(System.lineSeparator())
            .append("4. Найти заявку по ID").append(System.lineSeparator())
            .append("5. Найти заявки по имени").append(System.lineSeparator())
            .append("6. Выход").append(System.lineSeparator())
            .toString();

    // поле содержит дефолтный вывод в консоль.
    private final PrintStream stdout = System.out;
    // буфер для результата.
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void loadOutput() {
        System.out.println("execute before method");
        System.setOut(new PrintStream(this.out));
    }

    @After
    public void backOutput() {
        System.setOut(this.stdout);
        System.out.println("execute after method");
    }

    /**
     * тест showAllItems - вывод списка всех заявок.
     */
    @Test
    public void whenChooseTwoThenShowAllItems() {
        Tracker tracker = new Tracker();
        Item[] items = {new Item("test name1", "desc1", 123L),
                        new Item("test name2", "desc2", 124L)};
        for (Item x : items) {
            tracker.add(x);
        }
        Input input = new StubInput(new String[]{"1", "6"});
        // выполняем действия пишушиее в консоль.
        new StartUI(input, tracker).init();
        // проверяем результат вычисления
        assertThat(
                new String(this.out.toByteArray()),
                is(new StringBuilder()
                        .append(MENU).append(System.lineSeparator())
                        .append("------------ Список заявок на данный момент --------------").append(System.lineSeparator())
                        .append(items[0]).append(System.lineSeparator())
                        .append(items[1]).append(System.lineSeparator())
                        .append(MENU).append(System.lineSeparator())
                        .toString()
                )
        );
    }


    /**
     * тест findItemById - вывод заявки по заданному id.
     */
    @Test
    public void whenEnterIdThenFindId() {
        Tracker tracker = new Tracker();
        Item[] items = {new Item("test name1", "desc1", 123L),
                        new Item("test name2", "desc1", 124L),
                        new Item("test name3", "desc2", 125L)};
        for (Item x : items) {
            tracker.add(x);
        }
        Input input = new StubInput(new String[]{"4", items[2].getId(), "6"});
        new StartUI(input, tracker).init();
        assertThat(
                new String(this.out.toByteArray()),
                is(new StringBuilder()
                        .append(MENU).append(System.lineSeparator())
                        .append("------------ Поиск заявки по id --------------").append(System.lineSeparator())
                        .append("Найдена заявка : " + items[2]).append(System.lineSeparator())
                        .append(MENU).append(System.lineSeparator())
                        .toString()
                )
        );
    }

    /**
     * тест findItemsByName - вывод списка одноименных заявок.
     */
    @Test
    public void whenEnterNameItemThenFindItems() {
        Tracker tracker = new Tracker();
        Item[] items = {new Item("test name1", "desc1", 123L),
                        new Item("test name1", "desc1", 124L),
                        new Item("test name2", "desc2", 125L)};
        for (Item x : items) {
            tracker.add(x);
        }
        Input input = new StubInput(new String[]{"5", items[0].getName(), "6"});
        new StartUI(input, tracker).init();
        assertThat(
                new String(this.out.toByteArray()),
                is(new StringBuilder()
                        .append(MENU).append(System.lineSeparator())
                        .append("------------ Поиск одноименных заявок --------------").append(System.lineSeparator())
                        .append(items[0]).append(System.lineSeparator())
                        .append(items[1]).append(System.lineSeparator())
                        .append(MENU).append(System.lineSeparator())
                        .toString()
                )
        );
    }

    /**
     * тест createItem - добавление заявки.
     */
    @Test
    public void whenUserAddItemThenTrackerHasNewItemWithSameName() {
        // создаём Tracker
        Tracker tracker = new Tracker();
        // создаём StubInput с последовательностью действий.
        Input input = new StubInput(new String[]{"0", "test name", "desc", "00", "6"});
        // создаём StartUI и вызываем метод init()
        new StartUI(input, tracker).init();
        // проверяем, что нулевой элемент массива в трекере содержит имя, введённое при эмуляции.
        assertThat(tracker.findAll()[0].getName(), is("test name"));
    }

    /**
     * тест editItem - редактирование/замена заявки.
     */
    @Test
    public void whenUpdateThenTrackerHasUpdatedValue() {
        // создаём Tracker
        Tracker tracker = new Tracker();
        // напрямую добавляем заявку
        //Item item = tracker.add(new Item("test name", "desc", 123L));

        Item[] items = {new Item("test name1", "desc1", 123L),
                        new Item("test name2", "desc2", 124L),
                        new Item("test name3", "desc3", 125L)};
        for (Item x : items) {
            tracker.add(x);
        }

        // создаём StubInput с последовательностью действий(производим замену заявки)
        Input input = new StubInput(new String[]{"2", items[1].getId(), "test replace", "заменили заявку", "00", "6"});
        // создаём StartUI и вызываем метод init()
        new StartUI(input, tracker).init();
        // проверяем, что нужный элемент массива в трекере содержит имя, введённое при эмуляции.
        assertThat(tracker.findById(items[1].getId()).getName(), is("test replace"));
    }

    /**
     * тест deleteItem - удаление заявки.
     */
    @Test
    public void whenDeleteThenTrackerHasValue() {
        Tracker tracker = new Tracker();
        Item[] items = {new Item("test name1", "desc1", 123L),
                        new Item("test name2", "desc2", 124L),
                        new Item("test name3", "desc3", 125L)};
        for (Item x : items) {
            tracker.add(x);
        }
        Input input = new StubInput(new String[]{"3", items[1].getId(), "6"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findAll()[1].getName(), is("test name3"));
    }
}
