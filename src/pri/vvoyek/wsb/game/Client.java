package pri.vvoyek.wsb.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static int lastId = 0;

    public enum Type {
        EASY, STRICT, EVIL
    }
    public final Type type;
    private final int id;
    public final List<Project> projects = new ArrayList<>();

    private Client(Type type) {
        this.type = type;
        this.id = ++lastId;
    }

    public String toString() {
        return id + "_" + type.toString();
    }

    public int getExtraPaymentDelay() {
        int extra = 0;

        if (type.equals(Type.EASY)) {
            if (Game.nextInt(100) < 30) {
                extra = 7;
            }
        } else if (type.equals(Type.EVIL)) {
            if (Game.nextInt(100) < 30) {
                extra = 7;
            } else if (Game.nextInt(100) < 5) {
                extra = 30;
            }
        }

        if (extra > 0) {
            System.out.println("Masz pecha, termin płatności wydłużono o " + extra + " dni!");
        }
        return extra;
    }

    public void payBills(LocalDate today) {
        for (Project p : projects) {
            if (p.closed)
                continue;
            if (p.paymentDate.equals(today)) {
                if (p.hasBugs() && !acceptBugs()) {
                    System.out.println("Zerwano kontrakt na " + p.name);
                    p.closed = true;

                    while (p.next != null) {
                        p = p.next;
                        System.out.println("Zerwano kontrakt na " + p.name);
                        p.closed = true;
                    }
                    continue;
                }
                double payment = p.getPrice() - p.getDownPayment();
                if (p.deliveryDate.isAfter(p.deadline)) {
                    if (type.equals(Type.EASY) &&
                            p.deliveryDate.isBefore(p.deadline.plusDays(7)) &&
                            (Game.nextInt(100) < 20)) {
                        System.out.println("Masz szczęście, uniknięto kary!");
                    } else {
                        payment -= p.getPenalty();
                    }
                }
                if (isReliablePayer()) {
                    p.contractor.receivePayment(payment, p.name);
                    p.payment += payment;
                }
                p.closed = true;
            }
        }
    }

    private boolean isReliablePayer() {
        switch (type) {
            case EVIL:
                return Game.nextInt(100) < 1;
            default:
                return true;
        }
    }

    private boolean acceptBugs() {
        switch (type) {
            case EASY:
                return true;
            case STRICT:
                return Game.nextInt(100) < 50;
            case EVIL:
            default:
                return false;
        }
    }

    public static Client getRandomClient() {
        Type[] ta = Type.values();
        int i = Game.nextInt(ta.length);
        Type t = ta[i];
        return new Client(t);
    }
}
