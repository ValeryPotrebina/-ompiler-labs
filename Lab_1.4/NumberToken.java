public class NumberToken extends Token{
    private long number;
    public NumberToken(Position start, Position end, long number) {
        super(DomainTag.NUMBER, start, end);
        this.number = number;
    }

    public long getNumber() {
        return number;
    }
}
