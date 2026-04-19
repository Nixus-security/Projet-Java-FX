package com.ynov.escape.util;

import com.ynov.escape.model.Step;

import java.util.List;
import java.util.Map;

public final class Dialogues {

    private Dialogues() {}

    public static final Map<Step, List<String>> LINES = Map.of(
            Step.INTRO_DIALOGUE, List.of(
                    "Écoute-moi bien. Une bombe a été placée quelque part en ville, et tout repose sur toi. Nous n'avons pas de temps à perdre. Chaque seconde compte.",
                    "Voici la situation : tu vas devoir résoudre une série d'énigmes. Chacune te donnera des indices pour localiser la bombe. Le temps presse, mais nous avons encore une chance si tu agis rapidement et avec précision.",
                    "Je sais que ce n'est pas facile, mais je crois en toi. Nous avons les outils nécessaires, et tu as l'intelligence pour déchiffrer ces énigmes. Chaque réponse correcte nous rapproche de la solution.",
                    "Ne laisse pas la pression te faire trébucher. Résous les énigmes, trouve l'emplacement de la bombe, et nous pourrons la désamorcer avant qu'il ne soit trop tard. On compte sur toi. La ville compte sur toi."
            ),
            Step.MID_DIALOGUE_1, List.of(
                    "Bien joué. Tu as bien avancé jusqu'ici. Tu as résolu toutes les énigmes, et maintenant, nous avons une meilleure idée de l'endroit où la bombe pourrait être. Mais la tâche n'est pas encore terminée.",
                    "Maintenant, il te faut localiser l'emplacement exact. Pour cela, tu vas interroger des suspects. Certains te diront la vérité, d'autres mentiront. Ce sera à toi de discerner qui est fiable et qui ne l'est pas.",
                    "Le temps presse. Chaque erreur pourrait nous coûter cher, alors fais attention. Nous n'avons pas de marge pour les hésitations. Trouve où elle se cache, et on pourra passer à l'étape suivante.",
                    "Tu es notre seul espoir, et je sais que tu peux le faire. Trouve la bombe, localise-la avec précision. C'est à toi de mener cette mission à bien.",
                    "Allez, il ne reste plus beaucoup de temps. Trouve cette bombe, et sauve tout le monde."
            ),
            Step.MID_DIALOGUE_2, List.of(
                    "Tu as trouvé l'emplacement de la bombe. C'est un soulagement, mais ne te repose pas encore. Le plus difficile reste à venir.",
                    "Nous savons maintenant où elle se trouve, mais il faut encore la désamorcer. C'est une course contre la montre. Si tu échoues à désactiver la bombe, tout est fini. Mais je sais que tu as ce qu'il faut pour y arriver.",
                    "Tu as déjà prouvé que tu es capable de prendre les bonnes décisions. Ne laisse pas cette dernière étape t'intimider. C'est à toi de jouer maintenant. La ville, les gens, tout dépend de toi.",
                    "Bonne chance. Et rappelle-toi, le destin de tout le monde est entre tes mains."
            ),
            Step.VICTORY, List.of(
                    "Tu l'as fait… Tu as réussi à désamorcer la bombe et à sauver la ville. Je savais que tu en étais capable.",
                    "Grâce à toi, des vies ont été sauvées aujourd'hui. Tu as fait preuve de courage, d'intelligence et de détermination dans chaque étape de cette mission. Tu as tout donné, et ça a payé.",
                    "Bien joué, vraiment. Tu as prouvé qu'il n'y a rien que tu ne puisses accomplir. Je n'oublierai jamais ce jour."
            ),
            Step.DEFEAT, List.of(
                    "L'échec est total. La bombe a explosé.",
                    "Le Chef, qui avait placé toute sa confiance en toi, est mort dans l'explosion.",
                    "La ville a été détruite. Des vies ont été perdues. Tout est fini.",
                    "On est tous très déçu !"
            )
    );
}
