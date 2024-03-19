public abstract class Token {
    protected Fragment fragment;
    protected DomainTag domainTag;

    public Token(DomainTag domainTag, Position start, Position end) {
        this.fragment = new Fragment(start, end);
        this.domainTag = domainTag;
    }

    @Override
    public String toString() {
        return String.format("%6s  %-18s", domainTag.toString(), fragment);
    }
}
