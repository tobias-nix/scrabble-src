package edu.unibw.se.scrabble.server.data.impl.spring;

import edu.unibw.se.scrabble.server.data.AuthData;
import edu.unibw.se.scrabble.server.data.ScrabbleData;
import org.springframework.context.ApplicationContext;
import edu.unibw.se.scrabble.server.data.Data;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Bößendörfer
 */

public class SpringScrabbleData implements Data{

    private final SpringScrabbleDataWorker worker;
    private static ApplicationContext applicationContext;

    public SpringScrabbleData(){
        if (applicationContext==null){
            applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        }
        worker = applicationContext.getBean(SpringScrabbleDataWorker.class);
    }

    public SpringScrabbleData(String applicationContextFile){
        if(applicationContext == null){
            applicationContext = new ClassPathXmlApplicationContext(applicationContextFile);
        }
        worker = applicationContext.getBean(SpringScrabbleDataWorker.class);
    }

    @Override
    public AuthData getAuthData() {
        return worker;
    }

    @Override
    public ScrabbleData getScrabbleData() {
        return worker;
    }

    public void clear(){
        if(worker != null){
            worker.clear();
        }
    }

    public void fill(){
        worker.fill();
    }
}