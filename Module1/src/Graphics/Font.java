package Graphics;

public class Font {
    private static Spritesheet font = new Spritesheet("res/fonts/arial.png", 16);
    private static Sprite[] characters = Sprite.split(font);
    // charIndex serves as a lookup table
    private static String charIndex = "ABCDEFGHIJKLM" + // Splitting into multiple lines for ease of reading
                                        "NOPQRSTUVWXYZ" +
                                        "abcdefghijklm" +
                                        "nopqrstuvwxyz" +
                                        "0123456789.,‘" + // Using some special quotes from word
                                        "’“”;:!@$%()-+";
    public Font(){
    }

    public void render (int x, int y, String text, Screen screen){
        render(x,y,0, 0,text,screen); // 0 spacing, black colour
    }

    public void renderColor (int x, int y, int color, String text, Screen screen){
        render(x,y,0, color,text,screen); // 0 spacing
    }

    public void renderSpacing (int x, int y, int spacing, String text, Screen screen){
        render(x,y,spacing, 0,text,screen); // 0 spacing
    }

    public void render (int x, int y, int spacing, int color,  String text, Screen screen){
        int xOffset = 0;
        int line = 0;
        for (int i = 0; i < text.length(); i++) {
            xOffset += (16 + spacing);
            int yOffset = 0;
            char currentChar = text.charAt(i);
            //TODO turn next if to switch case, in ',' change the spacing for next char no bring it closer?
            if (currentChar == 'g' || currentChar == 'y' || currentChar == 'p' || currentChar == 'q' || currentChar == 'j' || currentChar == ',') yOffset = 4;
            if (currentChar == '\n') {
                line ++; // Make it change lines, 16 is size of letter
                xOffset = 0; // Reset line starting point
            }
            int index = charIndex.indexOf(currentChar); // find corresponding char's index
            if (index == -1) continue;
            screen.renderTextChar(x + xOffset, y + line*20 + yOffset, characters[index], color, false); // + i*16 to not stack them
        }
    }


}
