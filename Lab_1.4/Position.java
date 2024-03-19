public class Position implements Comparable<Position>{
    private String program;
    private int line;
    //номер в строке
    private int position;
    //Номер в программе
    private int index;

    public Position(String program) {
        this.program = program;
        this.line = 1;
        this.position = 1;
        this.index = 0;
    }
    public Position(Position otherPosition) {
        program = otherPosition.program;
        line = otherPosition.line;
        position = otherPosition.position;
        index = otherPosition.index;
    }

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }

    public int current(){
        return index == program.length() ? -1 : program.charAt(index);
    }

    public Position next(){
        if (index < program.length()){
            if (isNewLine()){
                if (program.charAt(index) == '\r'){
                    index++;
                }
                line++;
                position = 1;
            } else  {
                position++;
            }
            index++;
        }
        return this;
    }

    public boolean isNewLine() {
        if (index == program.length())
            return true;
        if (program.charAt(index) == '\r' && index + 1 < program.length())
            return (program.charAt(index + 1) == '\n');
        return program.charAt(index) == '\n';
    }

    @Override
    public int compareTo(Position o) {
        return this.index - o.index;
    }

    @Override
    public String toString() {
        return "(" + line + ", " + position + ")";
    }
}
