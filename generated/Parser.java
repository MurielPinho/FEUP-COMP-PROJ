/* Parser.java */
/* Generated By:JavaCC: Do not edit this line. Parser.java */
import java.util.*;
import java.io.*;

import java.io.StringReader;


public class Parser implements ParserConstants {
    private static int whileExpressionErrorsCounter;
    private static int whileExpressionParCounter;

    public Parser(String stringReader) throws ParseException, FileNotFoundException{
        this.whileExpressionErrorsCounter = 0;
        this.whileExpressionParCounter = 0;

        System.out.println("Parsing...");

        System.setIn(new FileInputStream(stringReader));
                Parser myProg = new Parser(System.in);
        myProg.Program();

                /*SimpleNode root = Program(); */ // returns reference to root node

                //root.dump(""); // prints the tree on the screen

                //System.out.println("Expression value: "+myCalc.eval(root));
    }

    /* Skips the while expression until if finds the token "{" */
    public static void skipWhileExpression() {
        Token prev; // will store the previous token

        do{
            prev = token;
            Token token = getNextToken();
        } while(token.kind != LBRACKET && token.kind != EOF);

        if(token.kind == LBRACKET)
            token = prev;
    }

    /* Handles with while expression error */
    public static void handleWhileExpressionError(ParseException exception) {
        System.out.println("Error on while");

        whileExpressionErrorsCounter++;
        if(whileExpressionErrorsCounter == 10) {
            System.out.println("STOP!!!");
        }

        skipWhileExpression();
    }

  static final public void Program() throws ParseException {
    ImportDeclaration();
    ClassDeclaration();
    jj_consume_token(0);
  }

  static final public void ImportDeclaration() throws ParseException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IMPORT:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      jj_consume_token(IMPORT);
      jj_consume_token(IDENTIFIER);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case DOT:{
          ;
          break;
          }
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        jj_consume_token(DOT);
        jj_consume_token(IDENTIFIER);
      }
      jj_consume_token(PVIRG);
    }
  }

  static final public void ClassDeclaration() throws ParseException {
    jj_consume_token(CLASS);
    jj_consume_token(IDENTIFIER);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case EXTENDS:{
      jj_consume_token(EXTENDS);
      jj_consume_token(IDENTIFIER);
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    jj_consume_token(LBRACKET);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INT:
      case BOOLEAN:
      case IDENTIFIER:{
        ;
        break;
        }
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      VarDeclaration();
    }
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PUBLIC:{
        ;
        break;
        }
      default:
        jj_la1[4] = jj_gen;
        break label_4;
      }
      MethodDeclaration();
    }
    jj_consume_token(RBRACKET);
  }

  static final public void VarDeclaration() throws ParseException {
    Type();
    VarDeclaration1();
  }

  static final public void VarDeclaration1() throws ParseException {
    jj_consume_token(IDENTIFIER);
    jj_consume_token(PVIRG);
  }

  static final public void MethodDeclaration() throws ParseException {
    jj_consume_token(PUBLIC);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INT:
    case BOOLEAN:
    case IDENTIFIER:{
      Type();
      jj_consume_token(IDENTIFIER);
      jj_consume_token(LPAR);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INT:
      case BOOLEAN:
      case IDENTIFIER:{
        Type();
        jj_consume_token(IDENTIFIER);
        label_5:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case VIRG:{
            ;
            break;
            }
          default:
            jj_la1[5] = jj_gen;
            break label_5;
          }
          jj_consume_token(VIRG);
          Type();
          jj_consume_token(IDENTIFIER);
        }
        break;
        }
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      jj_consume_token(RPAR);
      jj_consume_token(LBRACKET);
      MethodBody();
      jj_consume_token(RETURN);
      Expression();
      jj_consume_token(PVIRG);
      jj_consume_token(RBRACKET);
      break;
      }
    case STATIC:{
      jj_consume_token(STATIC);
      jj_consume_token(VOID);
      jj_consume_token(MAIN);
      jj_consume_token(LPAR);
      jj_consume_token(STRING);
      jj_consume_token(LRPAR);
      jj_consume_token(RRPAR);
      jj_consume_token(IDENTIFIER);
      jj_consume_token(RPAR);
      jj_consume_token(LBRACKET);
      MethodBody();
      jj_consume_token(PVIRG);
      jj_consume_token(RBRACKET);
      break;
      }
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void MethodBody() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IF:
    case INT:
    case NEW:
    case TRUE:
    case THIS:
    case WHILE:
    case FALSE:
    case BOOLEAN:
    case LBRACKET:
    case LPAR:
    case NOT:
    case IDENTIFIER:
    case INTEGERLITERAL:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        jj_consume_token(IDENTIFIER);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case IDENTIFIER:{
          VarDeclaration1();
          MethodBody();
          break;
          }
        case LRPAR:
        case DOT:
        case EQUAL:
        case AND:
        case LESS:
        case PLUS:
        case MINUS:
        case MULT:
        case DIV:{
          Statement2();
          label_6:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case IF:
            case NEW:
            case TRUE:
            case THIS:
            case WHILE:
            case FALSE:
            case LBRACKET:
            case LPAR:
            case NOT:
            case IDENTIFIER:
            case INTEGERLITERAL:{
              ;
              break;
              }
            default:
              jj_la1[8] = jj_gen;
              break label_6;
            }
            Statement();
          }
          break;
          }
        default:
          jj_la1[9] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
        }
      case INT:
      case BOOLEAN:{
        Type1();
        VarDeclaration1();
        MethodBody();
        break;
        }
      case IF:
      case NEW:
      case TRUE:
      case THIS:
      case WHILE:
      case FALSE:
      case LBRACKET:
      case LPAR:
      case NOT:
      case INTEGERLITERAL:{
        Statement1();
        label_7:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case IF:
          case NEW:
          case TRUE:
          case THIS:
          case WHILE:
          case FALSE:
          case LBRACKET:
          case LPAR:
          case NOT:
          case IDENTIFIER:
          case INTEGERLITERAL:{
            ;
            break;
            }
          default:
            jj_la1[10] = jj_gen;
            break label_7;
          }
          Statement();
        }
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[12] = jj_gen;
      ;
    }
  }

  static final public void Type() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INT:
    case BOOLEAN:{
      Type1();
      break;
      }
    case IDENTIFIER:{
      jj_consume_token(IDENTIFIER);
      break;
      }
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Type1() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INT:{
      jj_consume_token(INT);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LRPAR:{
        jj_consume_token(LRPAR);
        jj_consume_token(RRPAR);
        break;
        }
      default:
        jj_la1[14] = jj_gen;
        ;
      }
      break;
      }
    case BOOLEAN:{
      jj_consume_token(BOOLEAN);
      break;
      }
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Statement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IF:
    case NEW:
    case TRUE:
    case THIS:
    case WHILE:
    case FALSE:
    case LBRACKET:
    case LPAR:
    case NOT:
    case INTEGERLITERAL:{
      Statement1();
      break;
      }
    case IDENTIFIER:{
      jj_consume_token(IDENTIFIER);
      Statement2();
      break;
      }
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Statement1() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LBRACKET:{
      jj_consume_token(LBRACKET);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case IF:
        case NEW:
        case TRUE:
        case THIS:
        case WHILE:
        case FALSE:
        case LBRACKET:
        case LPAR:
        case NOT:
        case IDENTIFIER:
        case INTEGERLITERAL:{
          ;
          break;
          }
        default:
          jj_la1[17] = jj_gen;
          break label_8;
        }
        Statement();
      }
      jj_consume_token(RBRACKET);
      break;
      }
    case IF:{
      jj_consume_token(IF);
      jj_consume_token(LPAR);
      Expression();
      jj_consume_token(RPAR);
      Statement();
      jj_consume_token(ELSE);
      Statement();
      break;
      }
    case WHILE:{
      While();
      break;
      }
    case NEW:
    case TRUE:
    case THIS:
    case FALSE:
    case LPAR:
    case NOT:
    case INTEGERLITERAL:{
      Expression3();
      Expression1();
      jj_consume_token(PVIRG);
      break;
      }
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void While() throws ParseException {
    try {
      jj_consume_token(WHILE);
      WhileExpression();
    } catch (ParseException e) {
handleWhileExpressionError(e);
    }
    Statement();
  }

  static final public void WhileExpression() throws ParseException {
    jj_consume_token(LPAR);
    Expression();
    jj_consume_token(RPAR);
  }

  static final public void Statement2() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case EQUAL:{
      jj_consume_token(EQUAL);
      Expression();
      jj_consume_token(PVIRG);
      break;
      }
    case LRPAR:{
      jj_consume_token(LRPAR);
      Expression();
      jj_consume_token(RRPAR);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case EQUAL:{
        jj_consume_token(EQUAL);
        Expression();
        jj_consume_token(PVIRG);
        break;
        }
      default:
        jj_la1[19] = jj_gen;
        ;
      }
      break;
      }
    case DOT:
    case AND:
    case LESS:
    case PLUS:
    case MINUS:
    case MULT:
    case DIV:{
      Expression5();
      break;
      }
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Expression() throws ParseException {
    Expression2();
    Expression1();
  }

  static final public void Expression1() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case DOT:
    case AND:
    case LESS:
    case PLUS:
    case MINUS:
    case MULT:
    case DIV:{
      Expression5();
      break;
      }
    case LRPAR:{
      jj_consume_token(LRPAR);
      Expression2();
      Expression1();
      jj_consume_token(RRPAR);
      break;
      }
    default:
      jj_la1[21] = jj_gen;

    }
  }

  static final public void Expression2() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NEW:
    case TRUE:
    case THIS:
    case FALSE:
    case LPAR:
    case NOT:
    case INTEGERLITERAL:{
      Expression3();
      break;
      }
    case IDENTIFIER:{
      jj_consume_token(IDENTIFIER);
      break;
      }
    default:
      jj_la1[22] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Expression3() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INTEGERLITERAL:{
      jj_consume_token(INTEGERLITERAL);
      break;
      }
    case TRUE:{
      jj_consume_token(TRUE);
      break;
      }
    case FALSE:{
      jj_consume_token(FALSE);
      break;
      }
    case THIS:{
      jj_consume_token(THIS);
      break;
      }
    case NEW:{
      jj_consume_token(NEW);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        jj_consume_token(IDENTIFIER);
        jj_consume_token(LPAR);
        jj_consume_token(RPAR);
        break;
        }
      case INT:{
        jj_consume_token(INT);
        jj_consume_token(LRPAR);
        Expression2();
        Expression1();
        jj_consume_token(RRPAR);
        break;
        }
      default:
        jj_la1[23] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    case NOT:{
      jj_consume_token(NOT);
      Expression2();
      Expression1();
      break;
      }
    case LPAR:{
      jj_consume_token(LPAR);
      Expression2();
      Expression1();
      jj_consume_token(RPAR);
      break;
      }
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Expression5() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case AND:
    case LESS:
    case PLUS:
    case MINUS:
    case MULT:
    case DIV:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AND:{
        jj_consume_token(AND);
        break;
        }
      case LESS:{
        jj_consume_token(LESS);
        break;
        }
      case PLUS:{
        jj_consume_token(PLUS);
        break;
        }
      case MINUS:{
        jj_consume_token(MINUS);
        break;
        }
      case MULT:{
        jj_consume_token(MULT);
        break;
        }
      case DIV:{
        jj_consume_token(DIV);
        break;
        }
      default:
        jj_la1[25] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      Expression2();
      Expression1();
      break;
      }
    case DOT:{
      jj_consume_token(DOT);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LENGTH:{
        jj_consume_token(LENGTH);
        break;
        }
      case IDENTIFIER:{
        jj_consume_token(IDENTIFIER);
        jj_consume_token(LPAR);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case NEW:
        case TRUE:
        case THIS:
        case FALSE:
        case LPAR:
        case NOT:
        case IDENTIFIER:
        case INTEGERLITERAL:{
          Expression2();
          Expression1();
          label_9:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case VIRG:{
              ;
              break;
              }
            default:
              jj_la1[26] = jj_gen;
              break label_9;
            }
            jj_consume_token(VIRG);
            Expression2();
            Expression1();
          }
          break;
          }
        default:
          jj_la1[27] = jj_gen;
          ;
        }
        jj_consume_token(RPAR);
        break;
        }
      default:
        jj_la1[28] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[29] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public ParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[30];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2000000,0x0,0x8000000,0x4000400,0x100000,0x0,0x4000400,0x4200400,0x50074a00,0x0,0x50074a00,0x54074e00,0x54074e00,0x4000400,0x0,0x4000400,0x50074a00,0x50074a00,0x50074a00,0x0,0x0,0x0,0x40054800,0x400,0x40054800,0x0,0x0,0x40054800,0x800000,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x8,0x0,0x2000,0x0,0x20,0x2000,0x2000,0x6004,0x3f99,0x6004,0x6004,0x6004,0x2000,0x1,0x0,0x6004,0x6004,0x4004,0x10,0x1f99,0x1f89,0x6004,0x2000,0x4004,0x1f80,0x20,0x6004,0x2000,0x1f88,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[47];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 30; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 47; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
