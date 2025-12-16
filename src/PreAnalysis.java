/**
 * PreAnalysis interface for students to implement their algorithm selection logic
 * 
 * Students should analyze the characteristics of the text and pattern to determine
 * which algorithm would be most efficient for the given input.
 * 
 * The system will automatically use this analysis if the chooseAlgorithm method
 * returns a non-null value.
 */
public abstract class PreAnalysis {
    
    /**
     * Analyze the text and pattern to choose the best algorithm
     * 
     * @param text The text to search in
     * @param pattern The pattern to search for
     * @return The name of the algorithm to use (e.g., "Naive", "KMP", "RabinKarp", "BoyerMoore", "GoCrazy")
     *         Return null if you want to skip pre-analysis and run all algorithms
     * 
     * Tips for students:
     * - Consider the length of the text and pattern
     * - Consider the characteristics of the pattern (repeating characters, etc.)
     * - Consider the alphabet size
     * - Think about which algorithm performs best in different scenarios
     */
    public abstract String chooseAlgorithm(String text, String pattern);
    
    /**
     * Get a description of your analysis strategy
     * This will be displayed in the output
     */
    public abstract String getStrategyDescription();
}


/**
 * Default implementation that students should modify
 * This is where students write their pre-analysis logic
 */
class StudentPreAnalysis extends PreAnalysis {
    
    @Override
    public String chooseAlgorithm(String text, String pattern) {
        // TODO: Students should implement their analysis logic here

        int n = text.length();
        int m = pattern.length();

        if(m == 0){
            return "Naive";
        }

        if(m > n){
            return "Naive";
        }

        int alphabetSize = estimateAlphabetSize(text, pattern);
        boolean repeatingPrefix = hashRepeatingPrefix(pattern);
        double maxCharRatio = maxCharRepetitionRatio(pattern);

        if(m <= 3){
            return "Naive";
        }

        if(repeatingPrefix || maxCharRatio >= 0.6){
            return "KMP";
        }

        if(n >= 50000 && m >= 16){

            if(alphabetSize > 32){
                return "BoyerMoore";
            }
            else{
                return "RabinKarp";
            }
        }

        if( n >= 2000 && m >= 8){
            if(alphabetSize > 20){
                return "BoyerMoore";
            }
            else{
                return "KMP";
            }
        }

        


        // 
        // Example considerations:
        // - If pattern is very short, Naive might be fastest
        // - If pattern has repeating prefixes, KMP is good
        // - If pattern is long and text is very long, RabinKarp might be good
        // - If alphabet is small, Boyer-Moore can be very efficient
        //
        // For now, this returns null which means "run all algorithms"
        // Students should replace this with their logic
        
        return "BoyerMoore"; // Return null to run all algorithms, or return algorithm name to use pre-analysis
    }
    
    @Override
    public String getStrategyDescription() {
         return "Student strategy: \n"
            + "- Kisa patternlerde (<=3) Naive kullan\n"
            + "- Pattern tekrarli veya max karakter orani yuksekse (~>=0.6) KMP sec\n"
            + "- Cok uzun text ve uzun pattern (n>=50000, m>=16): alfabe buyukse BoyerMoore, kucukse RabinKarp\n"
            + "- Uzun text (n>=2000) ve m>=8: alfabe buyukse BoyerMoore, kucukse KMP\n"
            + "- Diger tum durumlarda default olarak BoyerMoore kullan";
    }

    private int estimateAlphabetSize(String text, String pattern) {
        boolean[] seen = new boolean[256];
        int count = 0;

        for (int i = 0; i < text.length(); i++) {
            int idx = text.charAt(i) & 0xFF;
            if (!seen[idx]) {
                seen[idx] = true;
                count++;
            }
        }

        for (int i = 0; i < pattern.length(); i++) {
            int idx = pattern.charAt(i) & 0xFF;
            if (!seen[idx]) {
                seen[idx] = true;
                count++;
            }
        }

        return count;
    }


    private boolean hashRepeatingPrefix(String pattern) {
        int m = pattern.length();
        if (m < 4){
            return false;
        }
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        lps[0] = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        // lps[m-1] = en uzun proper prefix-suffix uzunlugu
        return lps[m - 1] >= m / 3;
    }


     private double maxCharRepetitionRatio(String pattern) {
        int m = pattern.length();
        if (m == 0) {
            return 0.0;
        }
        int[] freq = new int[256];
        int maxFreq = 0;

        for (int i = 0; i < m; i++) {
            int idx = pattern.charAt(i) & 0xFF;
            freq[idx]++;
            if (freq[idx] > maxFreq) {
                maxFreq = freq[idx];
            }
        }

        return (double) maxFreq / (double) m;
    }

}


/**
 * Example implementation showing how pre-analysis could work
 * This is for demonstration purposes
 */
class ExamplePreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        int textLen = text.length();
        int patternLen = pattern.length();

        // Simple heuristic example
        if (patternLen <= 3) {
            return "Naive"; // For very short patterns, naive is often fastest
        } else if (hasRepeatingPrefix(pattern)) {
            return "KMP"; // KMP is good for patterns with repeating prefixes
        } else if (patternLen > 10 && textLen > 1000) {
            return "RabinKarp"; // RabinKarp can be good for long patterns in long texts
        } else {
            return "Naive"; // Default to naive for other cases
        }
    }

    private boolean hasRepeatingPrefix(String pattern) {
        if (pattern.length() < 2) return false;

        // Check if first character repeats
        char first = pattern.charAt(0);
        int count = 0;
        for (int i = 0; i < Math.min(pattern.length(), 5); i++) {
            if (pattern.charAt(i) == first) count++;
        }
        return count >= 3;
    }

    @Override
    public String getStrategyDescription() {
        return "Example strategy: Choose based on pattern length and characteristics";
    }
}

/**
 * Instructor's pre-analysis implementation (for testing purposes only)
 * Students should NOT modify this class
 */
class InstructorPreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        // This is a placeholder for instructor testing
        // Students should focus on implementing StudentPreAnalysis
        return null;
    }

    @Override
    public String getStrategyDescription() {
        return "Instructor's testing implementation";
    }
}
