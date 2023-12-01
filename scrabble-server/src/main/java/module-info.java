module scrabble.server {
    requires scrabble.common;
    requires java.rmi;

    requires jakarta.persistence;

    requires spring.data.jpa;
    requires spring.context;
    requires spring.beans;

    requires java.sql;
    requires org.hibernate.orm.core;

    opens edu.unibw.se.scrabble.server.data.impl.spring;
    opens edu.unibw.se.scrabble.server.data.impl.spring.jpa;

    exports edu.unibw.se.scrabble.server.data.impl.spring to spring.beans;

    exports edu.unibw.se.scrabble.server.auth;
    exports edu.unibw.se.scrabble.server.data;
}