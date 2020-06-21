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

    public void payBills(LocalDate today) {
        for (Project p : projects) {
            if (!p.wasFullyPaid() && p.paymentDate.equals(today)) {
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
                p.contractor.receivePayment(payment, p.name);
                p.payment = p.getPrice();
            }
        }
    }

    public static Client getRandomClient() {
        Type[] ta = Type.values();
        int i = Game.nextInt(ta.length);
        Type t = ta[i];
        return new Client(t);
    }
}
