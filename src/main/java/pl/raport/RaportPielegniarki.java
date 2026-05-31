package pl.raport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RaportPielegniarki {

    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== GENERATOR RAPORTU PIELĘGNIARSKIEGO ===\n");

        String data = pytajODate();
        String zmiana = pytajOZmiane();
        String oddzial = pytajString("Oddział: ");
        String pielegniarka = pytajString("Imię i nazwisko pielęgniarki zdającej: ");
        String pielegniarkaPrzyjmujaca = pytajString("Imię i nazwisko pielęgniarki przyjmującej: ");

        System.out.println("\n--- STAN ŁÓŻEK ---");
        int lozkaOgolem = pytajInt("Łóżka ogółem na oddziale: ");
        int lozkaZajete = pytajInt("Łóżka zajęte: ");
        int lozkaDostepne = lozkaOgolem - lozkaZajete;

        System.out.println("\n--- RUCH CHORYCH ---");
        List<String> przyjeci = pytajListePacjentow("przyjętych");
        List<String> wypisani = pytajListePacjentow("wypisanych");
        List<String> przeniesieni = pytajListePacjentow("przeniesionych (z/na inny oddział)");
        List<String> zgony = pytajListePacjentow("zgonów");

        System.out.println("\n--- ZABIEGI I PROCEDURY ---");
        List<String> przygotowaniDoZabiegow = pytajListePacjentow("pacjentów przygotowanych do zabiegów/operacji");
        List<String> poZabiegach = pytajListePacjentow("pacjentów po zabiegach wymagających szczególnej obserwacji");

        System.out.println("\n--- LEKI I ZLECENIA ---");
        String uwagiFarmacja = pytajString("Uwagi dotyczące leków/zleceń (lub '-' jeśli brak): ");

        System.out.println("\n--- SYTUACJE SZCZEGÓLNE ---");
        String sytuacjeSzczegolne = pytajString("Sytuacje szczególne, incydenty (lub '-' jeśli brak): ");

        System.out.println("\n--- DODATKOWE UWAGI ---");
        String uwagi = pytajString("Inne uwagi przekazywane w raporcie (lub '-' jeśli brak): ");

        String raport = generujRaport(
                data, zmiana, oddzial,
                pielegniarka, pielegniarkaPrzyjmujaca,
                lozkaOgolem, lozkaZajete, lozkaDostepne,
                przyjeci, wypisani, przeniesieni, zgony,
                przygotowaniDoZabiegow, poZabiegach,
                uwagiFarmacja, sytuacjeSzczegolne, uwagi
        );

        System.out.println("\n\n");
        System.out.println("=".repeat(60));
        System.out.println("         RAPORT GOTOWY DO SKOPIOWANIA:");
        System.out.println("=".repeat(60));
        System.out.println(raport);
        System.out.println("=".repeat(60));
    }

    static String generujRaport(
            String data, String zmiana, String oddzial,
            String pielegniarka, String pielegniarkaPrzyjmujaca,
            int lozkaOgolem, int lozkaZajete, int lozkaDostepne,
            List<String> przyjeci, List<String> wypisani,
            List<String> przeniesieni, List<String> zgony,
            List<String> przygotowaniDoZabiegow, List<String> poZabiegach,
            String uwagiFarmacja, String sytuacjeSzczegolne, String uwagi
    ) {
        StringBuilder sb = new StringBuilder();
        String linia = "-".repeat(60);

        sb.append("RAPORT PIELĘGNIARSKI\n");
        sb.append(linia).append("\n");
        sb.append("Data:        ").append(data).append("\n");
        sb.append("Zmiana:      ").append(zmiana).append("\n");
        sb.append("Oddział:     ").append(oddzial).append("\n");
        sb.append("Zdaje:       ").append(pielegniarka).append("\n");
        sb.append("Przyjmuje:   ").append(pielegniarkaPrzyjmujaca).append("\n");

        sb.append(linia).append("\n");
        sb.append("STAN ŁÓŻEK:\n");
        sb.append("  Ogółem:      ").append(lozkaOgolem).append("\n");
        sb.append("  Zajęte:      ").append(lozkaZajete).append("\n");
        sb.append("  Dostępne:    ").append(lozkaDostepne).append("\n");

        sb.append(linia).append("\n");
        sb.append("RUCH CHORYCH:\n");
        sb.append("  Przyjęci (").append(przyjeci.size()).append("):\n");
        formatujListe(sb, przyjeci);
        sb.append("  Wypisani (").append(wypisani.size()).append("):\n");
        formatujListe(sb, wypisani);
        sb.append("  Przeniesieni (").append(przeniesieni.size()).append("):\n");
        formatujListe(sb, przeniesieni);
        sb.append("  Zgony (").append(zgony.size()).append("):\n");
        formatujListe(sb, zgony);

        sb.append(linia).append("\n");
        sb.append("ZABIEGI / PROCEDURY:\n");
        sb.append("  Przygotowani do zabiegów (").append(przygotowaniDoZabiegow.size()).append("):\n");
        formatujListe(sb, przygotowaniDoZabiegow);
        sb.append("  Po zabiegach – szczególna obserwacja (").append(poZabiegach.size()).append("):\n");
        formatujListe(sb, poZabiegach);

        sb.append(linia).append("\n");
        sb.append("LEKI / ZLECENIA:\n");
        sb.append("  ").append(uwagiFarmacja).append("\n");

        sb.append(linia).append("\n");
        sb.append("SYTUACJE SZCZEGÓLNE:\n");
        sb.append("  ").append(sytuacjeSzczegolne).append("\n");

        sb.append(linia).append("\n");
        sb.append("UWAGI DODATKOWE:\n");
        sb.append("  ").append(uwagi).append("\n");

        sb.append(linia).append("\n");
        sb.append("Raport wygenerowano: ")
          .append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
          .append(" o godz. ")
          .append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
          .append("\n");
        sb.append("Podpis zdającej: ____________________\n");
        sb.append("Podpis przyjmującej: ________________\n");

        return sb.toString();
    }

    static void formatujListe(StringBuilder sb, List<String> lista) {
        if (lista.isEmpty()) {
            sb.append("    brak\n");
        } else {
            for (String p : lista) {
                sb.append("    - ").append(p).append("\n");
            }
        }
    }

    static String pytajODate() {
        System.out.print("Data raportu (zostaw puste = dziś " +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "): ");
        String odp = sc.nextLine().trim();
        if (odp.isEmpty()) {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
        return odp;
    }

    static String pytajOZmiane() {
        System.out.println("Zmiana:");
        System.out.println("  1. Ranna  (7:00 - 15:00 lub 7:00 - 19:00)");
        System.out.println("  2. Popołudniowa (15:00 - 23:00)");
        System.out.println("  3. Nocna  (23:00 - 7:00 lub 19:00 - 7:00)");
        System.out.println("  4. Inna (wpisz ręcznie)");
        System.out.print("Wybór (1-4): ");
        String wybor = sc.nextLine().trim();
        return switch (wybor) {
            case "1" -> "Ranna (7:00 – 15:00 / 7:00 – 19:00)";
            case "2" -> "Popołudniowa (15:00 – 23:00)";
            case "3" -> "Nocna (23:00 – 7:00 / 19:00 – 7:00)";
            default -> {
                System.out.print("Wpisz zmianę: ");
                yield sc.nextLine().trim();
            }
        };
    }

    static List<String> pytajListePacjentow(String kategoria) {
        List<String> lista = new ArrayList<>();
        System.out.println("Podaj pacjentów " + kategoria + " (Enter żeby pominąć, pusta linia kończy):");
        while (true) {
            System.out.print("  > ");
            String linia = sc.nextLine().trim();
            if (linia.isEmpty()) break;
            lista.add(linia);
        }
        return lista;
    }

    static String pytajString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int pytajInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Podaj liczbę całkowitą.");
            }
        }
    }
}
