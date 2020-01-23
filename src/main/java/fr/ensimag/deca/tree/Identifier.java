package fr.ensimag.deca.tree;
import java.util.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.codegen.RegisterManager;

/**
 * Deca Identifier
 *
 * @author gl53
 * @date 01/01/2020
 */
public class Identifier extends AbstractIdentifier {

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        // Set<Symbol> sym = localEnv.getParent().stringIsIn();
        // for (Symbol s:sym)
        // {
        //     System.out.println(s.getName());
        // }

        ExpDefinition def = localEnv.get(this.getName());
        if (def == null)
        {
                throw new ContextualError("Identificateur \""
                        + this.getName().toString()
                        + "\" non déclaré (règle 0.1)", this.getLocation());
        }

        this.setDefinition(def);
        this.setType(def.getType());
        return this.getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
      TypeDefinition def = compiler.get_env_types().get(this.getName());
      if (def == null){
              throw new ContextualError("Type \"" + this.getName().toString() +
                "\" n'est pas un type prédéfini (règle 0.2)", this.getLocation());
          }

          // Type type = this.getName().getType();
          Type type = def.getType();
          // TypeDefinition def = new TypeDefinition(type, Location.BUILTIN);
          // System.out.println(def.getType());
          this.setDefinition(def);
          this.setType(type);
          return this.getType();
    }

    public ExpDefinition verifydef(EnvironmentExp localEnv) throws ContextualError
    {
      ExpDefinition def = localEnv.get(this.getName());
      if (def == null)
      {
        throw new ContextualError("Identificateur \"" + this.getName().toString() +
          "\" is not defined at this class", this.getLocation());
      }
      this.setType(def.getType());
      this.setDefinition(def);
      return def;
    }
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }
    @Override
    public void codeGenIdent(DecacCompiler compiler,int i){
        RegisterOffset r = new RegisterOffset(i, Register.GB);
        ExpDefinition def = this.getExpDefinition();
        def.setOperand(r);
    }
    @Override
     protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), Register.R1));
        super.codeGenPrint(compiler, hex);
        //throw new UnsupportedOperationException("not yet implemented55");
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented55");
        GPRegister r1 = compiler.getRegisterManager().allocReg(compiler);
        compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), r1));
        return r1;
    }
    private static HashMap<LabelOperand, String> Vu = new HashMap<LabelOperand, String>();
    @Override
    protected void codeGenObj(DecacCompiler compiler){
        compiler.addComment("construction de la table des methodes de " + this.getType());
        LabelOperand obj = new LabelOperand(new Label("code.Object.equals"));
        compiler.addInstruction(new LOAD(new ImmediateNull(), Register.R0));
        RegisterOffset gb1 = new RegisterOffset(1, Register.GB);
        RegisterOffset gb2 = new RegisterOffset(2, Register.GB);
        compiler.addInstruction(new STORE(Register.R0, gb1));
        compiler.addInstruction(new LOAD(obj, Register.R0));
        Vu.put(obj, this.getType().toString());
        compiler.addInstruction(new STORE(Register.R0, gb2));
        this.getClassDefinition().setOperand(this.getClassDefinition().getType(),gb1);
    }


}
