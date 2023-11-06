
package javafxapplication1.util;

/**
 *
 * @author SHIHAN
 */
public class NowDate extends java.util.Date
{
    public static NowDate getUtilDate()
    {
       NowDate date=new NowDate();
       return date;
    }
    
    public static java.sql.Date getSqlDate()
    {
      java.sql.Date sqlDate= new java.sql.Date(getUtilDate().getTime());
      return sqlDate;
    }
}
