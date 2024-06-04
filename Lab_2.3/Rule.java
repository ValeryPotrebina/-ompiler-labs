import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;

public class Rule extends Tree{

    public ArrayList<Tree> children;
    public String ruleName;

    public Rule(String ruleName) {
        this.ruleName = ruleName;
        this.children =  new ArrayList<>();
    }

    public void setChildren(ArrayList<Tree> children) {
        this.children = children;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String printGraph() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n");
        Stack<Tree> stack = new Stack<>();
        stack.push(this);
        while (!stack.isEmpty()){
            Tree tree = stack.pop();
            String label = tree instanceof Rule ? ((Rule) tree).ruleName : ((SynToken) tree).tokenValue;
            sb.append("  ").append(tree.index).append(" [label=\"").append(label).append("\"]\n");
            if (tree instanceof Rule) {
                Rule rule = (Rule) tree;
                for (Tree child : rule.children) {
                    stack.push(child);
                }
            }
        }
        stack.empty();
        stack.push(this);
        while (!stack.isEmpty()){
            Tree tree = stack.pop();
            if (tree instanceof Rule) {
                Rule rule = (Rule) tree;
                for (Tree child : rule.children) {
                    stack.push(child);
                    sb.append("  ").append(tree.index).append(" -> ").append(child.index).append("\n");
                }
                if (rule.children.size() <= 1) {
                    continue;
                }
                sb.append("  { rank=same; ");
                for (int i = 0; i < rule.children.size(); i++) {
                    sb.append(rule.children.get(i).index);
                    if (i != rule.children.size() - 1) {
                        sb.append(" -> ");
                    }
                }
                sb.append(" [style=invis] }\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }


}
