package ru.akirakozov.sd.refactoring.database;

import java.io.PrintWriter;

public class Product {
    private final String name;
    private final Long price;

    public Product(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String toSqlValue() {
        return "(\"" + name + "\"," + price + ")";
    }

    public void write(PrintWriter writer) {
        writer.println(name + "\t" + price + "</br>");
    }
}
