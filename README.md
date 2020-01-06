# IFT2255_#GYM

IFT 2255 - Aut 2017
Devoir maison 3
Logiciel #GYM — Incrément 3 : Construction
Bien que vous avez déjà traité plusieurs itérations des workflows des exigences, analyse et conception durant les deux premiers incréments, le client a à nouveau précisé et ajouté certains besoins. Comme c'est un processus itératif et incrémental, il est normal de devoir apporter des ajustements aux artéfacts produits lors des DM1 et DM2. Toute modification devra être mentionnée dans votre rapport. Vous pouvez vous baser sur les corrigés sommaires présentés en classe ou votre version correcte. Vous êtes tenus de fournir une solution complète du projet pour cette livraison finale.

Ce devoir se concentre sur les workflows d'implémentation, de test et de livraison. Le but est de mettre en pratique l'implémentation de votre design, effectuer une vérification du système programmé et livrer le produit #GYM.

Révision des besoins pour le livrable 3
#GYM vient d'acquérir un nouveau dirigeant. Ils ont décidé de moderniser l'interaction avec le logiciel afin de réduire le travail de l'agent à la réception. Ils veulent maintenant que les membres et les professionnels utilisent une application mobile pour gérer leur compte. Notez que vous n'avez pas à implémenter l'application mobile. Vous trouverez en gras les changements dans les besoins en ce sens, ainsi que quelques précisions.

#GYM est un centre sportif qui offre des services pour différentes activités physiques. Les membres paient un frais d'adhésion mensuel à #GYM. Avec ce montant, ils ont droit à un accès illimité à la salle des machines, la piscine et les salles multisport (basket, soccer, tennis). Ils ont également droit à des entrainements et consultations illimitées avec des professionnels, notamment des experts en exercice physique (entraineur personnel, de yoga, de cross-fit) et des experts de la santé physique (nutritionniste, physiothérapeute, massothérapeute).

Pour adhérer à #GYM, le client doit se présenter à la réception du centre. L'agent lui demande son compte Facebook valide (c'est-à-dire son courriel), ainsi que d'autres informations personnelles. Il entre ces informations sur son ordinateur qui les enregistre dans un système central: le Centre de Données #GYM. Ceci crée le nouveau membre et, lorsque le client a payé son frais d'adhésion, lui assigne un numéro unique à neuf chiffres.

Le membre peut ensuite se loguer sur l'application #GYM, installée sur son appareil mobile intelligent, avec son compte Facebook. L'application communique avec le Centre de Données #GYM. Ce serveur vérifie le numéro de membre. Si le numéro est valide, le mot Validé apparait sur l'écran. Si le numéro est invalide, la raison est affichée, tel que « Numéro invalide » ou « Membre suspendu ». Ce dernier message indique que des frais sont dus (c'est-à-dire que le membre n'a pas payé ses frais de membre pour au moins un mois) et le statut de membre a été suspendu. Lorsque valide, le nom du membre, son numéro de membre et un code QR représentant ce numéro sont affichés sur l'écran de l'appareil mobile. Un lecteur de code QR est placé sur le tourniquet du centre pour lui permettre l'accès.

Pour participer à un cours de sport ou obtenir tout autre service d'un professionnel, le membre doit s'inscrire à la séance. Les inscriptions se font à partir de l'application mobile #GYM. Il peut consulter l'ensemble des services offerts et les séances disponibles pour le jour même dans le Répertoire des Services. Quand il sélectionne une séance, il faut confirmer son inscription. À ce moment, le logiciel crée un enregistrement sur le disque qui contient les champs suivants. L'application recherche et affiche ensuite le montant à payer pour ce service.

    Date et heure actuelles (JJ-MM-AAAA HH:MM:SS)
    Date à laquelle du service qui sera fourni (JJ-MM-AAAA)
    Numéro du professionnel (9 chiffres)
    Numéro du membre (9 chiffres)
    Code de la séance (7 chiffres)
    Commentaires (100 caractères) (facultatif).
Pour fournir un service chez #GYM, le professionnel doit se présenter à la réception du centre. Si c'est un nouveau professionnel, l'agent lui crée un compte de façon similaire à un membre. Pour créer une séance de service, l'agent envoie l'information ci-dessous au Centre de Données. Un professionnel de #GYM peut demander à l'agent de consulter les inscriptions à ses séances en envoyant une requête au Centre de Données.

    Date et heure actuelles (JJ-MM-AAAA HH:MM:SS)
    Date de début du service (JJ-MM-AAAA)
    Date de fin du service (JJ-MM-AAAA)
    Heure du service (HH:MM)
    Récurrence hebdomadaire du service (quels jours il est offert à la même heure)
    Capacité maximale (maximum 30 inscriptions)
    Numéro du professionnel (9 chiffres)
    Code de la séance (7 chiffres)
    Commentaires (100 caractères) (facultatif).
Un professionnel de #GYM utilise également l'application sur son appareil mobile. Il s'y logue aussi avec son compte Facebook. De là, il peut envoyer une requête au Centre de Données pour voir les inscriptions à ses séances. Avant que la séance ne commence, chaque membre doit présenter son code QR au professionnel. Le professionnel utilise le Répertoire des Services pour rechercher le code de la séance approprié à sept chiffres correspondant au service à fournir. Par exemple, 5984701 est le code pour une séance avec un nutritionniste, tandis que 8839484 est le code pour une séance de zumba. Les trois premiers chiffres correspondent au code du service, les deux prochains chiffres correspondent au numéro de la séance et les deux derniers chiffres correspondent aux deux derniers chiffres du numéro du professionnel. Par exemple, 8839484 et 8835484 sont deux séances de zumba offertes par le même professionnel. Le professionnel sélectionne la séance qui affiche toute l'information nécessaire. Ensuite, il peut sélectionner Confirmer inscriptions pour lire le code QR d'un membre. La requête est envoyée au Centre de Données qui vérifie si l'inscription est valide. Si c'est le cas, le mot Validé apparait à l'écran. Sinon, le membre se verra refusé l'accès à la séance. À ce moment, le logiciel crée un enregistrement sur le disque qui contient les champs ci-dessous. C'est la confirmation à l'avance que le service a été fournit.

    Date et heure actuelles (JJ-MM-AAAA HH:MM:SS)
    Numéro du professionnel (9 chiffres)
    Numéro du membre (9 chiffres)
    Code de la séance (7 chiffres)
    Commentaires (100 caractères) (facultatif).
Les vendredis à minuit, la procédure comptable principale est exécutée au Centre des Données #GYM. Cette procédure génère des enregistrements sur le disque sous forme de données de transfert électronique de fond (TEF). Le fichier TEF contient le nom du professionnel, le numéro du professionnel et le montant à lui transférer pour ces services de la semaine. Les systèmes informatiques des banques concernées vont plus tard s'assurer que le compte bancaire de chaque professionnel a reçu le montant approprié. Vous n'êtes responsables que de la création des fichiers TEF, pas de leur traitement.

Chaque membre, ayant suivi un service professionnel #GYM cette semaine-là, reçoit une liste des services qui lui ont été fournis, triée par ordre chronologique du service. La facture, accessible via l'application, contient l'information suivante :

    Nom du membre (25 lettres) 
    Numéro du membre (9 chiffres) 
    Adresse du membre (25 caractères) 
    Ville du membre (14 caractères) 
    Province du membre (2 lettres) 
    Code postal du membre (6 caractères).
    Pour chaque service fourni, les détails suivants sont requis :
        Date du service (JJ-MM-AAAA) 
        Nom du professionnel (25 lettres) 
        Nom du service (20 caractères).
Chaque professionnel ayant facturé #GYM cette semaine-là reçoit un avis de paiement dans l'application, contenant la liste des services qu'il a fourni aux membres de #GYM. À la fin de l'avis, on retrouve un résumé qui indique le nombre de consultations avec les membres et le total des frais pour cette semaine. C'est-à-dire, les champs du rapport sont :

    Nom du professionnel (25 lettres)
    Numéro du professionnel (9 chiffres)
    Adresse du professionnel (25 caractères)
    Ville du professionnel (14 caractères)
    Province du professionnel (2 lettres)
    Code postal du professionnel (6 caractères).
    Pour chaque service fourni, les détails suivants sont requis :
        Date du service (JJ-MM-AAA) 
        Date et heure à laquelle les données étaient reçues par l'ordinateur (JJ-MM-AAAA HH:MM:SS)
        Nom du membre (25 caractères)
        Numéro du membre (9 chiffres)
        Code de la séance (7 chiffres)
        Montant à payer (jusqu'à 999.99$), si facturé par inscription, sinon un seul montant est affiché.
La procédure comptable lit aussi le fichier des services fournis de la semaine et produit un rapport de synthèse qui est envoyé au gérant. Ce rapport peut aussi être exécuté individuellement à la demande du gérant de #GYM à n'importe quel moment de la semaine. Le rapport concerne les comptes payables et décrit la liste de tous les professionnels qui doivent être payés cette semaine-là, le nombre de services de chacun et le total de leurs frais pour cette semaine-là. À la fin du rapport, le nombre total de professionnels qui ont fourni des services cette semaine-là, le nombre total de services et le total des frais doivent apparaitre.

Le traitement des paiements des frais d'adhésion à #GYM a été confié aux services comptables RnB, une organisation tierce. RnB est responsable des procédures financières, comme l'enregistrement des paiements des frais d'adhésion, la suspension des membres dont les frais sont en retard et le rétablissement des membres suspendus qui ont maintenant payé les frais dus. Le système informatique de RnB met à jour les enregistrements d'adhésion pertinents du Centre de Données #GYM chaque soir à 21h.

Votre organisation a été octroyée le contrat de développer seulement le logiciel du Centre de Données de #GYM qui inclue la procédure comptable. Une autre organisation sera responsable des logiciels de communication, de l'application mobile, du logiciel requis par les services comptables de RnB, et du traitement des TEF. Le contrat stipule que, lors des tests d'acceptation, les interactions avec l'application mobile, l'ordinateur de l'agent sont simulées sur une console Java en tapant les touches du clavier et les données à afficher apparaissent à l'écran de la console. Chaque rapport de membre doit être écrit dans son propre fichier ; le nom du fichier doit commencer par le nom du membre, suivi de la date du rapport. Il en est de même pour les rapports de professionnels. Le Répertoire des Services est créé comme un fichier. Il n'y a pas de base de données dans ce projet. Pendant la même exécution de l'application, les données doivent être retenues (en mémoire). Mais si vous redémarrez l'application, ces données ne sont pas tenues d'exister.

Tâches
Tâche 1 : Implémentation en Java
Implémentez le logiciel en Java de telle sorte que toutes les fonctionnalités identifiées dans vos cas d'utilisation soient supportées. Vous pouvez partir du dernier prototype que vous avez déjà implémenté. Cependant, le logiciel n'est plus un prototype et doit fonctionner pour n'importe quel membre, professionnel et service. Votre diagramme de classe doit refléter l'implémentation. Les méthodes essentielles à implémenter sont déterminées par vos diagrammes de séquence. Assurez-vous d'avoir une méthode publique par diagramme de séquence. Vous serez évalués sur la cohérence entre les différents artéfacts. Cette tâche suppose que vous avez intégrer les derniers changements demandés par le client.

Entendez-vous sur les normes de programmation à utiliser. Vous formez une même équipe, donc le code de toute l'application doit être uniforme. N'oubliez pas d'insérer des commentaires dans le code là où nécessaire. La tâche d'implémentation doit être distribuée équitablement entre les membres de l'équipe : chaque membre de l'équipe doit être assigné à peu près le même nombre de classes à implémenter. La communication entre vous est très importante, en particulier communiquez avec la personne responsable d'un module avec lequel votre module intéragit. Le code doit être écrit en utilisant l'environnement Eclipse qui offre un support indispensable au débogage.

Notez que vous n'êtes pas tenus de fournir une interface utilisateur graphique. Un menu par ligne de commande est suffisant.

Tâche 2 : Tests unitaires
Choisissez six méthodes publiques (des diagrammes de séquence) à tester. Le nombre de méthodes testées doit être réparti équitablement entre les membres de l'équipe. Les tests unitaires doivent être implémentés en utilisant JUnit. Vous devez tester une méthode que vous n'avez pas implémentée : si Alice a implémenté la méthode m() et Bob a implémenté la méthode p(), alors Alice doit tester unitairement p() et Bob doit tester unitairement m().

Tous les tests implémentés doivent passer avec succès. Incluez une capture d'écran de la fenêtre JUnit qui montre que tous les tests unitaires passent.

Tâche 3 : Livraison et documentation
Une fois le code écrit, testé et complété, vous devez générer les JavaDocs à partir du code. Ceci aidera les programmeurs futurs à trouver et mieux comprendre les fonctionnalités que vous avez implémentées. Ceci les aidera également de maintenir le code et interagir avec votre programme.

Incluez un programme ANT qui permet de déployer le programme en un fichier JAR. Vous pouvez voir les instructions pour le faire dans Eclipse ici. Attention, assurez-vous de sélectionner "Runnable JAR file" pour qu'il soit exécutable. Si vous utilisez des librairies externes, assurez-vous qu'elles font partie de votre code: c'est à vous de gérer les dépendances.

Produisez un manuel utilisateur 500 mots (+/-10%) qui décrit comment utiliser l'application #GYM que vous avez développée au cours du trimestre.

Vous devez également inclure tous les artéfacts produits lors des itérations précédentes (cas d'utilisation, diagrammes d'activités, diagramme de classe, diagrammes de séquence) qui serviront de documentation du projet. Tel que mentionné pour la tâche 1, assurez-vous que tous les artéfacts (code et modèles) sont cohérents. Organisez votre rapport par workflow.

Collaboration
À la fin du rapport, affichez une capture d'écran des statistiques et du pourcentage de propriété de votre dépôt BitBucket.

Assurez-vous de communiquer régulièrement au sein de votre équipe. Utilisez l'application Slack de votre équipe pour discuter et échanger. De ce fait, vous conservez une trace de vos décisions pour les itérations ultérieures. Pour faciliter la collaboration en équipe, vous devez utiliser le système de contrôle de révision BitBucket. Faites régulièrement des soumissions BitBucket (commit/push) de tous vos fichiers. Vous serez évalués sur la bonne utilisation de votre dépôt.

L'auxiliaire du cours est le représentant du client. Si vous avez besoin de clarifications et de précisions, communiquez avec lui directement par Slack ou en personne.

Barème
Code source Java du programme [50%]. Soummettre un programme qui ne compile pas aboutira à 0/50.
Tests unitaires en JUnit [20%]
Configuration ANT et production du JAR [5%]
Manuel utilisateur [8%]
JavaDocs générés [7%]
Documentation du projet et cohérence entre les modèles et le code [10%]
Téléversez toutes les images, fichiers source, fichiers de données, projet VPP et y faire référence dans le rapport. Le fichier ZIP doit comprendre sept dossiers appelés : Exigence, Analyse, Conception, Implementation, Tests, API et Documentation.

Ressources supplémentaires
Vous devez utiliser Visual Paradigm pour tous les diagrammes UML. Vous pouvez le télécharger, l'installer et l'activer à partir du lien disponible sur StudiUM.

Pour BitBucket vous pouvez utiliser le client EGit dans Eclipse.

Pour communiquer en équipe vous pouvez utiliser Slack.

Informations pratiques
Le devoir vaut 15% de la note finale.

Voir la date de remise sur StudiUM à 23h55. Tout retard engendrera une pénalité de 5% par jour pour un maximum de deux jours.

Le devoir est à faire en équipe d'au plus trois. Aucun devoir effectué seul ne sera accepté. Vous pouvez changer d'équipe pour ce devoir, mais c'est votre responsabilité de contacter l'auxiliaire pour changer vos accès (Slack, BitBucket, VPP en ligne).

La remise du devoir est un fichier ZIP qui comprend un fichier HTML simple (rapport.htm) ainsi que tous les fichiers additionnels nécessaires (.jpg, .java, .txt, .vpp, tous les autres fichiers que vous voulez remettre). Le rapport doit avoir un lien explicite à tous les fichiers et afficher toutes les images directement sur la page. Vous devez inscrire dans l'entête du rapport : le nom de tous les membres de votre équipe, les quatre derniers chiffres de vos matricules, vos courriels et le temps mis par chaque membre sur le devoir (pour des raisons statistiques uniquement). Votre solution doit être incluse en entier dans le body du rapport. Inscrivez toutes vos hypothèses. Puis décrivez votre solution pour chaque tâche sous forme de rapport.

De plus, le rapport doit inclure une section Distribution des tâches. Cette section doit énumérer toutes les tâches accomplies et le pourcentage de contribution de chaque membre par tâche. Si les pourcentages ne sont pas plus ou moins égaux, la note peut différer d'un membre à l'autre. Vous pouvez trouver un exemple du rapport ici.

Une seule personne par équipe remet le devoir complet sur StudiUM. Les autres membres doivent uniquement soumettre le fichier rapport avec les noms et la distribution des tâches (ce n'est pas grave si les images n'apparaissent pas ou si les liens ne fonctionnent pas). Indiquez la personne qui soumet le devoir complet.

Omettre le rapport engendrera une pénalité de 5%. Omettre les statistiques BitBucket engendrera une autre pénalité de 5%.
