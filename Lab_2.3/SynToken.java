import java.util.Optional;

public class SynToken extends Tree{
    public Type tokenType;
    public String tokenValue;


    public SynToken(Type tokenType) {
        this.tokenType = tokenType;
    }
}
