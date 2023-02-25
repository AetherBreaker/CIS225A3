// Write a program that accepts a die type, number of dice, and target number.
// Valid die types are: 4, 6, 8, 10, 12, 20, 100 Max number of dice:
// 10 Min & max target numbers: 5 & 30 Roll the dice.
// Each die is a separate attempt at the target number with the following caveats:
// If more than 50% of the dice are ones, the result is a bust and the roll fails.
// If any of the results are the same as the die type, that individual result is
// open ended, and another dice is rolled and added to the first result.
// This can happen multiple times.
// You will have to use Java random.

package chapter6;

import java.util.Random;
import java.util.ArrayList;

public class Dice {

    private static enum ArgType {
        die("-die"),
        dice("-dice"),
        target("-target"),
        count("-count"),
        help("-help"),
        defaul("default");

        private String value;

        private ArgType(String value) {
            this.value = value;
        }

        public static ArgType fromString(String text) {
            if (text != null) {
                for (ArgType b : ArgType.values()) {
                    if (text.equalsIgnoreCase(b.value)) {
                        return b;
                    }
                }
            }
            return ArgType.defaul;
        }
    }

    private static enum DieType {
        d4(4),
        d6(6),
        d8(8),
        d10(10),
        d12(12),
        d20(20),
        d100(100);

        private int value;

        private DieType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static DieType fromString(String text) {
            if (text != null) {
                for (DieType b : DieType.values()) {
                    if (text.equalsIgnoreCase(b.name())) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    private DieType die_type;
    private int dice_count;
    private int target_number;
    private ArrayList<String> roll_results = new ArrayList<String>();

    /**
     * Accepts a die type, number of dice, and target number
     * 
     * @param die_type_as_string The die type to roll (eg D4, D6, D8, D10, D12, D20,
     *                           D100) case insensitive
     * @param dice_count         The number of dice to roll
     * @param target_number      The target number to roll
     */
    public Dice(String die_type_as_string, int dice_count, int target_number) {
        setDieType(die_type_as_string);
        setDiceCount(dice_count);
        setTargetNumber(target_number);
    }

    /**
     * Accepts a simplified version of dice notation (eg 2d6 or 4d20)
     * 
     * @param dice_notation The dice notation to parse
     * @param target_number The target number to roll
     */
    public Dice(String dice_notation, int target_number) {
        String[] parsed_dice_notation = parseDiceNotation(dice_notation);
        setDieType(parsed_dice_notation[0]);
        setDiceCount(Integer.parseInt(parsed_dice_notation[1]));
        setTargetNumber(target_number);
        roll();
    }

    /**
     * Accepts a simplified version of dice notation (eg 2d6 or 4d20)
     * 
     * @param dice_notation The dice notation to parse
     */
    private String[] parseDiceNotation(String dice_notation) {
        String[] parsed_dice_notation = new String[2];
        String[] split_dice_notation = dice_notation.split("d");
        if (split_dice_notation.length != 2) {
            throw new IllegalArgumentException("Invalid dice notation");
        }
        parsed_dice_notation[0] = "d" + split_dice_notation[1];
        parsed_dice_notation[1] = split_dice_notation[0];
        return parsed_dice_notation;
    }

    // I am intentionally only creating setters here as the intended interface is in
    // the constructor
    // the results of each class instance are final and so there is no reason to
    // change the dice parameters the class contains after results are calculated
    // if you want to roll again, you create a new class instance
    // NO CHEATING YOUR ROLLS :P

    // The reason I still create setters is just to abstract the checks that each
    // member variable must pass when being set.
    // Just so everything is organized rather than being mixed together inside
    // the constructor

    /**
     * Sets the die type to roll
     * 
     * @param die_type The die type to roll (eg D4, D6, D8, D10, D12, D20, D100)
     *                 case insensitive
     */
    private void setDieType(String die_type) {
        DieType die_type_enum = DieType.fromString(die_type);

        // formatting is only weird here in an attempt to keep the line from extending
        // past 80 characters
        if (die_type_enum == null) {
            throw new IllegalArgumentException(
                    "Invalid die type: You must choose from: "
                            +
                            "D4, D6, D8, D10, D12, D20, or D100");
        }
        this.die_type = die_type_enum;
    }

    /**
     * Sets the number of dice to roll
     * 
     * @param dice_count The number of dice to roll
     */
    private void setDiceCount(int dice_count) {
        if (dice_count < 1 || dice_count > 100) {
            throw new IllegalArgumentException(
                    "Invalid dice count: You cannot have less than 1 or more than 100 dice");
        }
        this.dice_count = dice_count;
    }

    /**
     * Sets the target number to roll
     * 
     * @param target_number The target number to roll
     */
    private void setTargetNumber(int target_number) {
        if (target_number < 5 || target_number > 30) {
            throw new IllegalArgumentException(
                    "Invalid target number: You cannot have a target number "
                            +
                            "less than 5 or more than 30");
        }
        this.target_number = target_number;
    }

    /**
     * Rolls the dice, this is only called once per class instance, and is called
     * automatically when the class is instantiated
     */
    private void roll() {
        if (this.roll_results.size() > 0) {
            throw new IllegalStateException("You cannot roll the dice more than once");
        }
        int successes = 0;
        int ones_count = 0;
        int dice_count = this.dice_count;
        Random random = new Random();

        for (int i = 0; i < dice_count; i++) {
            int roll_total = 0;
            int roll = random.nextInt(this.die_type.getValue()) + 1;
            String result_entry = "Roll " + (i + 1) + ": " + this.die_type.name() + "(" + roll + ")";
            if (roll == 1) {
                ones_count++;
            }
            roll_total += roll;
            while (roll == this.die_type.getValue()) {
                dice_count++;
                roll = random.nextInt(this.die_type.getValue()) + 1;
                result_entry += ", " + this.die_type.name() + "(" + roll + ")";
                if (roll == 1) {
                    ones_count++;
                }
                roll_total += roll;
            }
            result_entry += " = " + roll_total;
            if (roll_total >= this.target_number) {
                successes++;
                result_entry += " SUCCESS";
            } else {
                result_entry += " FAIL";
            }
            this.roll_results.add(result_entry);
        }
        this.roll_results.add("You rolled " + successes + " successes");
        if (ones_count > 0) {
            this.roll_results.add("You rolled " + ones_count + " ones");
            if (ones_count >= dice_count / 2) {
                this.roll_results.add("You rolled more than half of your dice as ones!");
                this.roll_results.add("BUST!");
            }
        }
    }

    public ArrayList<String> getRollResultAsList() {
        return this.roll_results;
    }

    public String getRollResultAsString() {
        return String.join("\n", this.roll_results);
    }

    public void printRollResult() {
        System.out.println(getRollResultAsString());
    }

    // accept command line arguments in the form of: <die_type> <dice_count>
    // <target_number>
    // or <dice_notation> <target_number>
    public static void main(String[] args) {
        int dice_count = 0;
        int target_number = 0;
        String die_type = null;
        String dice_notation = null;
        int i = 0;
        for (String arg : args) {
            switch (ArgType.fromString(arg)) {
                case count -> {
                    dice_count = Integer.parseInt(args[i + 1]);
                }
                case die -> {
                    die_type = args[i + 1];
                }
                case dice -> {
                    dice_notation = args[i + 1];
                }
                case target -> {
                    target_number = Integer.parseInt(args[i + 1]);
                }
                case help -> {
                    System.out.println("Usage: java Dice -die <die_type> -count <dice_count> -target <target_number>");
                    System.out.println("Usage: java Dice -dice <dice_notation> -target <target_number>");
                    System.out.println("Example: java Dice -die d20 -count 3 -target 15");
                    System.out.println("Example: java Dice -dice 3D20 -target 15");
                    System.exit(0);
                }
                case defaul -> {
                    if (arg.startsWith("-")) {
                        throw new IllegalArgumentException("Invalid argument: " + arg);
                    }
                }
            }
            i++;
        }

        if (target_number < 1) {
            throw new IllegalArgumentException("You must specify a target number");
        }

        if (dice_notation != null) {
            Dice dice = new Dice(dice_notation, target_number);
            dice.printRollResult();
        } else {
            if (dice_count < 1) {
                throw new IllegalArgumentException("You must specify a dice count");
            }
            if (die_type != null) {
                Dice dice = new Dice(die_type, dice_count, target_number);
                dice.printRollResult();
            } else {
                throw new IllegalArgumentException("You must specify a die type or dice notation");
            }
        }
    }
}