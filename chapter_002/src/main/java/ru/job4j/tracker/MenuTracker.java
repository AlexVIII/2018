package ru.job4j.tracker;

import java.util.*;

/**
 * Реализация меню трекера.
 *
 *@author John Ivanov (johnivo@mail.ru)
 *@version $Id$
 *@since 08.01.2019
 */
public class MenuTracker {

    /**
     * Хранит ссылку на объект.
     */
    private final Input input;
    /**
     * Хранит ссылку на объект.
     */
    private final Tracker tracker;
    /**
     * Хранит ссылку на объект.
     */

    private final List<UserAction> actions = new ArrayList<>();

    /**
     * Конструктор.
     *
     * @param input   объект типа Input.
     * @param tracker объект типа Tracker.
     */
    public MenuTracker(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     * Метод для получения массива меню.
     *
     * @return длину массива.
     */
    public int getActionsLength() {
        return this.actions.size();
    }

    /**
     * Метод заполняет массив.
     */
    public void fillActions(StartUI startUI) {
        this.actions.add(new AddItem(0, "Add new Item."));
        this.actions.add(new ShowItems(1, "Show all items."));
        this.actions.add(new EditItem(2, "Edit item."));
        this.actions.add(new DeleteItem(3, "Delete item."));
        this.actions.add(new FindItemById(4, "Find item by Id."));
        this.actions.add(new FindItemsByName(5, "Find items by name."));
        this.actions.add(new ExitProgram(6, "Exit Program."));
    }

    /**
     * Метод в зависимости от указанного ключа, выполняет соотвествующие действие.
     *
     * @param key ключ операции.
     */
    public void select(int key) {
        this.actions.get(key).execute(this.input, this.tracker);
    }

    /**
     * Метод выводит на экран меню.
     */
    public void show() {
        for (UserAction action : this.actions) {
            if (action != null) {
                System.out.println(action.info());
            }
        }
    }

    /**
     * 0 Добавление новой заявки.
     */
    public class AddItem extends BaseAction {

        public AddItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("------------ Adding new item --------------");
            String name = input.ask("Please, provide item name:");
            String desc = input.ask("Please, provide item description:");
            String created = input.ask("Please, provide item time created:");
            Item item = new Item(name, desc, Long.parseLong(created));
            tracker.add(item);
            System.out.println("New Item: " + item.toString());
        }
    }

    /**
     * 1 Вывод списока всех заявок.
     */
    public class ShowItems extends BaseAction {

        public ShowItems(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("------------ All available items: --------------");
            Item[] items = tracker.findAll();
            if (items.length != 0) {
                for (Item item : items) {
                    System.out.println(item.toString());
                }
            } else {
                System.out.println("------------ No items available. -----------");
            }
        }
    }

    /**
     * 2 Редактирование выбранной заявки.
     */
    public class EditItem extends BaseAction {

        public EditItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("------------ Editing item: --------------");
            String id = input.ask("Please, provide id item:");
            String name = input.ask("Please, edit item name:");
            String desc = input.ask("Please, edit item description:");
            String created = input.ask("Please, edit item create:");
            Item item = new Item(name, desc, Long.parseLong(created));
            if (tracker.replace(id, item)) {
                System.out.println("Item with id:" + id + " edited!");
            } else {
                System.out.println("------------ No item with such id. -----------");
            }
        }
    }

    /**
     * 3 Удаление выбранной заявки.
     */
    public class DeleteItem extends BaseAction {

        public DeleteItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("------------ Deleting item: --------------");
            String id = input.ask("Please, provide id item:");
            if (tracker.delete(id)) {
                System.out.println("Item with id:" + id + " deleted!");
            } else {
                System.out.println("------------ No item with such id. -----------");
            }
        }
    }

    /**
     * 4 Поиск заявки по id.
     */
    public class FindItemById extends BaseAction {

        public FindItemById(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("------------ Find item by id: --------------");
            String id = input.ask("Please, provide id item:");
            Item item = tracker.findById(id);
            if (item != null) {
                System.out.println("Item found: " + item.toString());
            } else {
                System.out.println("------------ No item with such id. -----------");
            }
        }
    }

    /**
     * 5 Поиск одноименных заявок.
     */
    public class FindItemsByName extends BaseAction {

        public FindItemsByName(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("------------ Find item by name: --------------");
            String nameItem = input.ask("Please, provide name item:");
            Item[] items = tracker.findByName(nameItem);
            if (items.length != 0) {
                for (Item item : items) {
                    System.out.println(item.toString());
                }
            } else {
                System.out.println("------------ No items with such name. -----------");
            }
        }
    }

    /**
     * 6 Выход из программы.
     */
    public class ExitProgram extends BaseAction {

        public ExitProgram(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {

            }
    }
}