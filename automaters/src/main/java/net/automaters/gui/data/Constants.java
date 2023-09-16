package net.automaters.gui.data;

import javax.swing.*;

public abstract class Constants
{

    public static class quests
    {
        public static final JCheckBox[] QUESTS =
                {
                new JCheckBox("Cook's Assistant"),
                new JCheckBox("Sheep Shearer"),
                new JCheckBox("Romeo & Juliet"),
                new JCheckBox("Pirates Treasure"),
                new JCheckBox("Ernest the Chicken"),
                new JCheckBox("The Restless Ghost"),
                new JCheckBox("Rune Mysteries"),
                new JCheckBox("Witch's Potion"),
                new JCheckBox("The Knight's Sword"),
                new JCheckBox("Doric's Quest"),
                new JCheckBox("Imp Catcher"),
                new JCheckBox("Goblin Diplomacy"),
                new JCheckBox("The Giant Dwarf"),
                new JCheckBox("Plague City"),
                new JCheckBox("Witch's House"),
                new JCheckBox("Elemental Workshop I"),
                new JCheckBox("Elemental Workshop II"),
                new JCheckBox("Misthalin Mystery"),
                new JCheckBox("Dwarf Cannon"),
                new JCheckBox("Murder Mystery"),
                new JCheckBox("Fishing Contest"),
                new JCheckBox("Sea Slug"),
                new JCheckBox("The Feud"),
                new JCheckBox("Priest in Peril"),
                new JCheckBox("Animal Magnetism"),
                new JCheckBox("Biohazard"),
                new JCheckBox("Underground Pass"),
                new JCheckBox("RFD - Cook"),
                new JCheckBox("RFD - Dwarf"),
                new JCheckBox("Vampyre Slayer"),
                new JCheckBox("Waterfall Quest"),
                new JCheckBox("Fight Arena"),
                new JCheckBox("Tree Gnome Village"),
                new JCheckBox("The Grand Tree")
        };
    }

    public static class accountCategory
    {
        public static final String[] ALL_CATEGORIES =
                {
                "Testing",
                "Full Access",
                "PVM",
                "Money Making",
                "Ironman",
                "PVP",
                "Free Builds",
                "Mules"
        };
    }

    public static class accountType
    {
        public static final String[] FREE_BUILDS =
                {
                "Tutorial Island",
                "F2P Med Level Main",
                "F2P Low Level Main",
                "F2P Med Level Skiller",
                "F2P Low Level Skiller"
                };

        public static final String[] FULL_ACCESS =
                {
                "Maxed Account",
                "Maxed Main",
                "Maxed Skiller",
                "P2P High Level Main",
                "Dragon Slayer II",
                "Sins of the Father",
                "Song of the Elves"
                };

        public static final String[] IRONMAN =
                {
                "Ironman High Level Main",
                "Ironman Med Level Main",
                "Ironman Skiller",
                "Ironman Starter",
                "HCIM 99 Agility",
                "HCIM 99 Firemaking",
                "HCIM 99 Fishing"
        };

        public static final String[] MONEY_MAKING = {
                "Blast Furnace",
                "Master Farmer",
                "Minnows",
                "Scarabs",
                "Spidines",
                "Undead Druids",
                "Blood Crafter",
                "Mort Myre Fungus",
        };

        public static final String[] PVM = {
                "Avas",
                "Barrows",
                "Corporeal Beast",
                "God Wars Dungeon",
                "NMZ",
                "Revenants",
                "Zulrah",
                "F2P High Level",
                "P2P Med Level",
                "P2P Low Level"
        };

        public static final String[] PVP = {
                "1 Defence Pure",
                "1 Defence Maxed Pure",
                "Obby Mauler",
                "Zerker",
                "Void Pure",
                "Maxed Med"
        };

        public static final String[] TESTING = {
                "Alpha Tester",
                "Beta Tester"
        };

        public static final String[] MULES = {
                "GP Mule"
        };
    }

    public static class sfEdition {
        public static final String[] EDITION = {
                "Pro Edition",
                "Standard Edition"
        };
    }

    public static class farmingOptions {

        public static final String[] TITHE_FARM = {
                "Enable - Tithe Farm after Farming run",
                "Enable - Tithe Farm ONLY",
                "Disable - Tithe Farm"
        };

        public static final String[] HERBS = {
                "Progressive",
                "Don't Use",
                "Guam",
                "Marrentill",
                "Tarromin",
                "Harralander",
                "Ranarr",
                "Toadflax",
                "Irit",
                "Avantoe",
                "Kwuarm",
                "Snapdragon",
                "Cadantine",
                "Lantadyme",
                "Dwarf weed",
                "Torstol"
        };

        public static final String[] ALLOTMENTS = {
                "Progressive",
                "Don't Use",
                "Potato",
                "Onion",
                "Cabbage",
                "Tomato",
                "Sweetcorn",
                "Strawberry",
                "Watermelon",
                "Snape grass"
        };

        public static final String[] FLOWERS = {
                "Progressive",
                "Don't Use",
                "Marigold",
                "Rosemary",
                "Nasturtium",
                "Woad seed",
                "Limpwurt",
                "White lily",
                "Nasturtium - Protect Watermelon Allotments",
                "White lily - Protect All Allotments"
        };

        public static final String[] BUSHES = {
                "Progressive",
                "Don't Use",
                "Redberry",
                "Cadavaberry",
                "Dwellberry",
                "Jangerberry",
                "Whiteberry",
                "Poison ivy berry"
        };

        public static final String[] CACTUS = {
                "Potato cactus",
                "Don't Use"
        };

        public static final String[] FARMING = {
                "Attempt farming run after every task, if patches are ready",
                "Only initiate farming if task is selected"
        };

        public static final String[] COMPOST = {
                "Use best",
                "Bottomless compost bucket",
                "Ultracompost",
                "Supercompost",
                "Compost"
        };

        public static final String[] MAKE_COMPOST = {
                "Yes - Create Ultracompost",
                "No - Don't create Ultracompost"
        };

    }
}