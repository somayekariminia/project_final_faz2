package ir.maktab.project_final_faz2.catcha;

public class CaptchaString {
            public static String Generate(int len){
                String str = "abcdefghijklmnopqrstuvwxyzABCD"
                        +"EFGHIJKLMNOPQRSTUVWXYZ0123456789";

                int n=str.length();
                String otp="";
                for(int i=0;i<len;i++){
                    otp+=str.charAt((int)(Math.random()*100)%n);
                }
                return otp;
            }
            public static void main(String[] args) {
                String ans = Generate(6);
                System.out.println(ans);
            }
        }

