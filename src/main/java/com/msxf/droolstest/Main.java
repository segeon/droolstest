package com.msxf.droolstest;

import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.FactHandle;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class Main {
    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kContainer = kieServices.getKieClasspathContainer();
        StatelessKieSession kieSession2 = kContainer.newStatelessKieSession("KSession2_2");
        //statefulSessionTest(kContainer);
        testSingleFact(kieSession2);
        //testMultipleFacts(kieServices, kieSession2);
    }

    private static void statefulSessionTest(KieContainer kContainer) {
        final KieSession statefulSession = kContainer.newKieSession("KSession2_1");
        String[] roomNames = new String[]{"kichen", "bedroom", "livingroom", "wc"};
        Map<String, Room> name2Room = new HashMap<>();
        for (String name : roomNames) {
            Room room = new Room(name);
            statefulSession.insert(room);
            Sprinkler sprinkler = new Sprinkler();
            sprinkler.setRoom(room);
            statefulSession.insert(sprinkler);
            name2Room.put(name, room);
        }
        new Thread(new Runnable() {
            public void run() {
                Scanner scanner = new Scanner(System.in);
                List<FactHandle> fireHanles = new ArrayList<>();
                while (scanner.hasNext()) {
                    String cmd = scanner.next();
                    switch (cmd) {
                        case "setfire1":
                           fireHanles.add( statefulSession.insert(new Fire(name2Room.get(roomNames[0]))));
                            break;
                        case "setfire2":
                            fireHanles.add(statefulSession.insert(new Fire(name2Room.get(roomNames[1]))));
                            break;
                        case "setfire3":
                            fireHanles.add(statefulSession.insert(new Fire(name2Room.get(roomNames[2]))));
                            break;
                        case "setfire4":
                            fireHanles.add(statefulSession.insert(new Fire(name2Room.get(roomNames[3]))));
                            break;
                        case "putoutfire":
                            for (FactHandle fireHanle : fireHanles) {
                                statefulSession.delete(fireHanle);
                            }
                            break;
                    }
                    statefulSession.fireAllRules();
                }
            }
        }).start();
        statefulSession.fireAllRules();
    }

    private static void testMultipleFacts(KieServices kieServices, StatelessKieSession kieSession2) {
        KieCommands commands = kieServices.getCommands();
        ArrayList<Command> list = new ArrayList<Command>();
        Application application = getApplication();
        list.add(commands.newInsert(application));
        Goods goods = createGoods();
        list.add(commands.newInsert(goods));
        kieSession2.execute(commands.newInsertElements(list));
        assertEquals(false, goods.isApproved());
        assertEquals(false, application.isApproved());
    }

    private static void testSingleFact(StatelessKieSession kieSession2) {
        Application application = getApplication();
        kieSession2.execute(application);
        assertEquals(application.isApproved(), false);
    }

    private static Application getApplication() {
        Application application = new Application();
        application.setName("thh");
        String  today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        application.setBirthday(new Date());
        application.setLoanAmount(20000);
        application.setLoanTerm(12);
        application.setIntRate(new BigDecimal("0.005"));
        application.setSex("F");
        return application;
    }

    private static Goods createGoods() {
        Goods goods = new Goods();
        goods.setName("mobile phone");
        goods.setBrand("iphone");
        goods.setAmount(1);
        goods.setPrice(6000);
        return goods;
    }
}
