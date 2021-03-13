/* ParserTokenManager.java */
/* Generated By:JJTree&JavaCC: Do not edit this line. ParserTokenManager.java */
import java.util.*;
import java.io.*;
import java.io.StringReader;

/** Token Manager. */
@SuppressWarnings("unused")public class ParserTokenManager implements ParserConstants {

  /** Debug output. */
  public static  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public static  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private static final int jjStopStringLiteralDfa_0(int pos, long active0){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x100000000004L) != 0L)
            return 0;
         if ((active0 & 0xffffe00L) != 0L)
         {
            jjmatchedKind = 45;
            return 4;
         }
         return -1;
      case 1:
         if ((active0 & 0x200L) != 0L)
            return 4;
         if ((active0 & 0xffffc00L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 1;
            return 4;
         }
         return -1;
      case 2:
         if ((active0 & 0xffff000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 2;
            return 4;
         }
         if ((active0 & 0xc00L) != 0L)
            return 4;
         return -1;
      case 3:
         if ((active0 & 0x1f000L) != 0L)
            return 4;
         if ((active0 & 0xffe0000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 3;
            return 4;
         }
         return -1;
      case 4:
         if ((active0 & 0xff00000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 4;
            return 4;
         }
         if ((active0 & 0xe0000L) != 0L)
            return 4;
         return -1;
      case 5:
         if ((active0 & 0xc000000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 5;
            return 4;
         }
         if ((active0 & 0x3f00000L) != 0L)
            return 4;
         return -1;
      default :
         return -1;
   }
}
private static final int jjStartNfa_0(int pos, long active0){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
static private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
static private int jjMoveStringLiteralDfa0_0(){
   switch(curChar)
   {
      case 33:
         return jjStopAtPos(0, 34);
      case 38:
         return jjMoveStringLiteralDfa1_0(0x8000000000L);
      case 40:
         return jjStopAtPos(0, 30);
      case 41:
         return jjStopAtPos(0, 31);
      case 42:
         return jjStopAtPos(0, 43);
      case 43:
         return jjStopAtPos(0, 41);
      case 44:
         return jjStopAtPos(0, 37);
      case 45:
         return jjStopAtPos(0, 42);
      case 46:
         return jjStopAtPos(0, 35);
      case 47:
         jjmatchedKind = 44;
         return jjMoveStringLiteralDfa1_0(0x4L);
      case 59:
         return jjStopAtPos(0, 38);
      case 60:
         return jjStopAtPos(0, 40);
      case 61:
         return jjStopAtPos(0, 36);
      case 83:
         return jjMoveStringLiteralDfa1_0(0x400000L);
      case 91:
         return jjStopAtPos(0, 32);
      case 93:
         return jjStopAtPos(0, 33);
      case 98:
         return jjMoveStringLiteralDfa1_0(0x4000000L);
      case 99:
         return jjMoveStringLiteralDfa1_0(0x80000L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x8002000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x40000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x2000600L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x800000L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x8000L);
      case 110:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x100000L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0x1000000L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x200000L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x14000L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x20000L);
      case 123:
         return jjStopAtPos(0, 28);
      case 125:
         return jjStopAtPos(0, 29);
      default :
         return jjMoveNfa_0(2, 0);
   }
}
static private int jjMoveStringLiteralDfa1_0(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 38:
         if ((active0 & 0x8000000000L) != 0L)
            return jjStopAtPos(1, 39);
         break;
      case 42:
         if ((active0 & 0x4L) != 0L)
            return jjStopAtPos(1, 2);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x48000L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x1800800L);
      case 102:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(1, 9, 4);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x30000L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x82000L);
      case 109:
         return jjMoveStringLiteralDfa2_0(active0, 0x2000000L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x4001000L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000L);
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x600000L);
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x100000L);
      case 120:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
static private int jjMoveStringLiteralDfa2_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x280000L);
      case 98:
         return jjMoveStringLiteralDfa3_0(active0, 0x100000L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x39000L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x40000L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000000L);
      case 112:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000L);
      case 116:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(2, 10, 4);
         return jjMoveStringLiteralDfa3_0(active0, 0x9000000L);
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
      case 119:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(2, 11, 4);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
static private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 100:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(3, 12, 4);
         break;
      case 101:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(3, 13, 4);
         else if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(3, 14, 4);
         return jjMoveStringLiteralDfa4_0(active0, 0x8000000L);
      case 103:
         return jjMoveStringLiteralDfa4_0(active0, 0x800000L);
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x400000L);
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x4120000L);
      case 110:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(3, 15, 4);
         break;
      case 111:
         return jjMoveStringLiteralDfa4_0(active0, 0x2000000L);
      case 115:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(3, 16, 4);
         return jjMoveStringLiteralDfa4_0(active0, 0xc0000L);
      case 116:
         return jjMoveStringLiteralDfa4_0(active0, 0x200000L);
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
static private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(4, 17, 4);
         else if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(4, 18, 4);
         return jjMoveStringLiteralDfa5_0(active0, 0x4000000L);
      case 105:
         return jjMoveStringLiteralDfa5_0(active0, 0x300000L);
      case 110:
         return jjMoveStringLiteralDfa5_0(active0, 0x8400000L);
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x3000000L);
      case 115:
         if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(4, 19, 4);
         break;
      case 116:
         return jjMoveStringLiteralDfa5_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
static private int jjMoveStringLiteralDfa5_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa6_0(active0, 0x4000000L);
      case 99:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(5, 20, 4);
         else if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(5, 21, 4);
         break;
      case 100:
         return jjMoveStringLiteralDfa6_0(active0, 0x8000000L);
      case 103:
         if ((active0 & 0x400000L) != 0L)
            return jjStartNfaWithStates_0(5, 22, 4);
         break;
      case 104:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(5, 23, 4);
         break;
      case 110:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(5, 24, 4);
         break;
      case 116:
         if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(5, 25, 4);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
static private int jjMoveStringLiteralDfa6_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(6, 26, 4);
         break;
      case 115:
         if ((active0 & 0x8000000L) != 0L)
            return jjStartNfaWithStates_0(6, 27, 4);
         break;
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
static private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 6;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 46)
                        kind = 46;
                     { jjCheckNAdd(5); }
                  }
                  else if (curChar == 36)
                  {
                     if (kind > 45)
                        kind = 45;
                     { jjCheckNAdd(4); }
                  }
                  else if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 0:
                  if (curChar != 47)
                     break;
                  if (kind > 1)
                     kind = 1;
                  { jjCheckNAdd(1); }
                  break;
               case 1:
                  if ((0xffffffffffffdbffL & l) == 0L)
                     break;
                  if (kind > 1)
                     kind = 1;
                  { jjCheckNAdd(1); }
                  break;
               case 3:
                  if (curChar != 36)
                     break;
                  if (kind > 45)
                     kind = 45;
                  { jjCheckNAdd(4); }
                  break;
               case 4:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  { jjCheckNAdd(4); }
                  break;
               case 5:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  { jjCheckNAdd(5); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 2:
               case 4:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  { jjCheckNAdd(4); }
                  break;
               case 1:
                  if (kind > 1)
                     kind = 1;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 6 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static private int jjMoveStringLiteralDfa0_1(){
   switch(curChar)
   {
      case 42:
         return jjMoveStringLiteralDfa1_1(0x80L);
      default :
         return 1;
   }
}
static private int jjMoveStringLiteralDfa1_1(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      return 1;
   }
   switch(curChar)
   {
      case 47:
         if ((active0 & 0x80L) != 0L)
            return jjStopAtPos(1, 7);
         break;
      default :
         return 2;
   }
   return 2;
}
static final int[] jjnextStates = {
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, "\151\146", 
"\151\156\164", "\156\145\167", "\166\157\151\144", "\145\154\163\145", "\164\162\165\145", 
"\155\141\151\156", "\164\150\151\163", "\167\150\151\154\145", "\146\141\154\163\145", 
"\143\154\141\163\163", "\160\165\142\154\151\143", "\163\164\141\164\151\143", 
"\123\164\162\151\156\147", "\154\145\156\147\164\150", "\162\145\164\165\162\156", 
"\151\155\160\157\162\164", "\142\157\157\154\145\141\156", "\145\170\164\145\156\144\163", "\173", 
"\175", "\50", "\51", "\133", "\135", "\41", "\56", "\75", "\54", "\73", "\46\46", 
"\74", "\53", "\55", "\52", "\57", null, null, };
static protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

static int curLexState = 0;
static int defaultLexState = 0;
static int jjnewStateCnt;
static int jjround;
static int jjmatchedPos;
static int jjmatchedKind;

/** Get the next Token. */
public static Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   switch(curLexState)
   {
     case 0:
       try { input_stream.backup(0);
          while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_0();
       break;
     case 1:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_1();
       if (jjmatchedPos == 0 && jjmatchedKind > 8)
       {
          jjmatchedKind = 8;
       }
       break;
   }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else
        {
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

static private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
static private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
static private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

    /** Constructor. */
    public ParserTokenManager(SimpleCharStream stream){

      if (input_stream != null)
        throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);

    input_stream = stream;
  }

  /** Constructor. */
  public ParserTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Reinitialise parser. */
  static public void ReInit(SimpleCharStream stream)
  {
    jjmatchedPos = jjnewStateCnt = 0;
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }

  static private void ReInitRounds()
  {
    int i;
    jjround = 0x80000001;
    for (i = 6; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  static public void ReInit(SimpleCharStream stream, int lexState)
  {
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  static public void SwitchTo(int lexState)
  {
    if (lexState >= 2 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
   "COMMENTS",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, -1, 1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x7ffffffffe01L, 
};
static final long[] jjtoSkip = {
   0x1feL, 
};
    static protected SimpleCharStream  input_stream;

    static private final int[] jjrounds = new int[6];
    static private final int[] jjstateSet = new int[2 * 6];

    
    static protected char curChar;
}
