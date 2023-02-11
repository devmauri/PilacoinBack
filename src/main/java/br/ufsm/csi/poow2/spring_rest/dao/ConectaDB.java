package br.ufsm.csi.poow2.spring_rest.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConectaDB {

    private final String DRIVER = "org.postgresql.Driver";
    private final String TIPO = "jdbc:postgresql";
    private final String IP = "127.0.0.1"; //servidor remoto
    private final String PORTA = "5432";
    private final String DB = "Lista";
    private final String USER = "postgres";
    private final String PASSWORD = "1234";
    private final String URL = this.TIPO + ":" + "//" + this.IP + ":" + this.PORTA + "/" + this.DB;

    public Connection getConnection() {

        Connection conn = null;

        try {
            Class.forName(this.DRIVER);
            conn = DriverManager.getConnection(
                    this.URL,
                    this.USER,
                    this.PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

}
