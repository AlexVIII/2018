package ru.job4j.warehouse;

import java.time.LocalDateTime;

/**
 * @author John Ivanov (johnivo@mail.ru)
 * @since 29.07.2019
 */
public class Meat extends Food {

    public Meat(String name, LocalDateTime createDate, LocalDateTime expiryDate, Double price, Double discount) {
        super(name, createDate, expiryDate, price, discount);
    }

    @Override
    public boolean canReproduct() {
        return false;
    }

    @Override
    public boolean lowTemp() {
        return false;
    }
}
