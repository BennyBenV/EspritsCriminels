package com.enquete;

import com.enquete.model.Crime;
import com.enquete.model.Indice;
import com.enquete.model.QuestionReponse;
import com.enquete.model.Suspect;
import com.enquete.service.CrimeServiceIA;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\uD83D\uDD0D Génération de l'enquête via IA...");
        Crime crime = CrimeServiceIA.genererCrimeParIA();
        System.out.println("✅ Enquête générée !");

        System.out.println("=== ENQUÊTE : Meurtre de " + crime.getVictime() + " ===\n");

        System.out.println("\n--- Indices ---");
        for (Indice i : crime.getIndices()) {
            System.out.println("- " + i);
        }

        System.out.println("\n--- Suspects ---");
        List<Suspect> suspects = crime.getSuspects();
        for (int i = 0; i < suspects.size(); i++) {
            System.out.println((i + 1) + ". " + suspects.get(i).getNom());
        }

        // Boucle principale d'interrogatoire ou verdict
        while (true) {
            System.out.println("\nQui veux-tu interroger ? (1-" + suspects.size() + ", ou 'verdict') : ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("verdict")) {
                System.out.println("Qui accuses-tu ? (Nom du suspect) : ");
                String nomAccuse = scanner.nextLine().trim();
                Suspect accuse = suspects.stream()
                        .filter(s -> s.getNom().equalsIgnoreCase(nomAccuse))
                        .findFirst()
                        .orElse(null);

                if (accuse == null) {
                    System.out.println("Ce nom n'est pas dans la liste des suspects.");
                } else {
                    if (accuse == crime.getCoupable()) {
                        System.out.println("Bonne réponse ! " + accuse.getNom() + " est coupable.");
                    } else {
                        System.out.println("Mauvaise réponse. Le vrai coupable était : " + crime.getCoupable().getNom());
                    }
                    System.out.println("Motif : jalousie professionnelle");
                    break;
                }
            } else {
                try {
                    int choix = Integer.parseInt(input) - 1;
                    if (choix >= 0 && choix < suspects.size()) {
                        Suspect s = suspects.get(choix);
                        List<QuestionReponse> questions = s.getQuestions();

                        System.out.println("\nQuestions disponibles pour " + s.getNom() + " :");
                        for (int j = 0; j < questions.size(); j++) {
                            System.out.println((j + 1) + ". " + questions.get(j).getQuestion());
                        }

                        System.out.println("Pose ta question (1-" + questions.size() + ") : ");
                        int q = Integer.parseInt(scanner.nextLine()) - 1;
                        if (q >= 0 && q < questions.size()) {
                            QuestionReponse qr = questions.get(q);
                            System.out.println(">> " + s.getNom() + " répond : \"" + qr.getReponse() + "\"");
                        } else {
                            System.out.println("Numéro de question invalide.");
                        }
                    } else {
                        System.out.println("Numéro de suspect invalide.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrée invalide.");
                }
            }
        }
        scanner.close();
    }

    // Ancien générateur d'exemple, conservé en commentaire
    /*
    public static Crime genererCrimeExemple() {
        Indice couteau = new Indice("Couteau", "Un couteau ensanglanté retrouvé dans la cuisine.", true);
        Indice gants = new Indice("Gants", "Des gants tachés de sang cachés sous le lit.", false);

        Map<String, String> repBob = Map.of(
                "où étiez-vous à 22h ?", "J'étais au travail tard",
                "connaissiez-vous la victime ?", "Oui, c'était ma collègue",
                "avez-vous bu quelque chose d'étrange ?", "Non rien du tout."
        );

        Map<String, String> repClaire = Map.of(
                "où étiez-vous à 22h ?", "Je regardais un film",
                "connaissiez-vous la victime ?", "Oui, on était dans la même école",
                "avez-vous bu quelque chose d'étrange ?", "J'ai entendu des bruits étranges dehors."
        );

        Map<String, String> repDavid = Map.of(
                "où étiez-vous à 22h ?", "Je dormais, seul.",
                "connaissiez-vous la victime ?", "Juste de vue",
                "avez-vous bu quelque chose d'étrange ?", "Quelqu'un rôdait près de chez elle."
        );

        List<Indice> indices = new ArrayList<>(List.of(couteau, gants));

        // Exemple obsolète avec anciens suspects
        return new Crime("Alice Moreau", List.of(), indices, null);
    }
    */
}
