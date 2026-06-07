package general;

import java.util.Scanner;

public class Validaciones{
    public static String leerNombreUsuario(Scanner sc){
        String valor = "";
        boolean valido = false;

        do{
            System.out.print("Ingresar nombre de usuario: ");
            valor = sc.nextLine();

            if(valor.trim().isEmpty()){
                System.out.println("No puedes dejar espacios vacios");
                
            }else if(valor.length() < 3){
                System.out.println("El nombre de usuario debe tener minimo 3 letras");
                
            }else if(valor.length() > 10){
                System.out.println("El nombre de usuario debe tener maximo 10 letras");
                
            }else if(valor.contains(" ")){
                System.out.println("El nombre de usuario no puede tener espacios");
                
            }else{
                
                boolean letrasNumeros = true;

                for(int i = 0; i < valor.length(); i++){
                    
                    char c = valor.charAt(i);

                    if(!Character.isLetterOrDigit(c)){
                        letrasNumeros = false;
                    }
                }

                if(!letrasNumeros){
                    System.out.println("El nombre de usuario solo puede contener letras y numeros");
                    
                }else{
                    valido = true;
                }
            }
        }
        while(!valido);

        return valor.trim();
    }

    public static String leerNombre(Scanner sc, String mensaje, boolean opcional){
        String valor = "";
        boolean valido = false;

        do{
            System.out.print("Ingrese su " + mensaje + ": ");
            valor = sc.nextLine();

            if(opcional && valor.trim().isEmpty()){
                valido = true;
                
            }else if(valor.trim().isEmpty()){
                System.out.println("No puedes dejar espacios vacios");
                
            }else if(valor.trim().length() < 3){
                System.out.println("Debe tener minimo 3 letras");
                
            }else if(valor.trim().length() > 30){
                System.out.println("Debe tener maximo 30 letras");
                
            }else{
                
                boolean letras = true;

                for(int i = 0; i < valor.trim().length(); i++){
                    
                    char c = valor.trim().charAt(i);

                    if(!Character.isLetter(c) && c != ' '){
                        letras = false;
                    }
                }

                if(!letras){
                    System.out.println("Solo puedes ingresar letras");
                    
                }else{ 
                    valido = true;
                }
            }
        }
        while(!valido);

        return valor.trim();
    }

    public static String leerContrasena(Scanner sc){
        String valor = "";
        boolean valido = false;

        do{
            System.out.print("Ingresar comtrasena: ");
            valor = sc.nextLine();

            if(valor.trim().isEmpty()){
                System.out.println("No puedes dejar la contrasena vacia");
                
            }else if(valor.length() < 8){
                System.out.println("La contrasena debe tener minimo 8 caracteres.");
                
            }else{
                boolean tieneMayuscula = false;
                boolean tieneMinuscula = false;
                boolean tieneNumero = false;
                boolean tieneEspecial = false;

                for(int i = 0; i < valor.length(); i++){
                    char c = valor.charAt(i);

                    if(Character.isUpperCase(c)){
                        tieneMayuscula = true;
                        
                    }if(Character.isLowerCase(c)){
                        tieneMinuscula = true;
                        
                    }if(Character.isDigit(c)){
                        tieneNumero = true;
                        
                    }if(!Character.isLetterOrDigit(c)){
                        tieneEspecial = true;
                    }
                }
                    if(!tieneMayuscula){
                    System.out.println("La contrasena debe contener al menos una mayuscula.");
                    
                }else if(!tieneMinuscula){
                    System.out.println("La contrasena debe contener al menos una minuscula.");
                    
                }else if(!tieneNumero){
                    System.out.println("La contrasena debe contener al menos un numero.");
                    
                }else if(!tieneEspecial){
                    System.out.println("La contrasena debe contener al menos un caracter especial.");
                    
                }else{
                    valido = true;
                }
            }
        }
        while(!valido);

        return valor;
    }

    public static int leerEdad(Scanner sc){
        int edad = 0;
        boolean valido = false;

        do{
            System.out.print("Ingresar edad: ");
            String entrada = sc.nextLine();
            boolean numero = true;

            for(int i = 0; i < entrada.length(); i++){
                char c = entrada.charAt(i);

                if(!Character.isDigit(c)){
                    numero = false;
                }
            }

            if(!numero || entrada.trim().isEmpty()){
                System.out.println("Ingrese un numero valido");
                
            }else{
                edad = Integer.parseInt(entrada.trim());

                if(edad <= 22){
                    System.out.println("La edad ingresada no es valida, debe ser entre los 22 y 40 años");
                    
                }else if(edad >= 40){
                    System.out.println("La edad ingresada no es valida, debe ser entre los 22 y 40 años");
                    
                }else{
                    valido = true;
                }
            }
        }
        while(!valido);

        return edad;
    }

    public static String leerReporte(Scanner sc, String mensaje){
        String valor = "";
        boolean valido = false;

        do{
            System.out.print(mensaje + "Debe tener maximo 50 letras");
            valor = sc.nextLine();

            if(valor.trim().isEmpty()){
                System.out.println("No puedes dejar el reporte vacio");
                
            }else if(valor.trim().length() > 50){
            System.out.print(mensaje + "Tiene maximo 50 letras");
            
            }else{
                valido = true;
            }
        }
        while(!valido);

        return valor.trim();
    }

    public static String leerTextoNoVacio(Scanner sc, String mensaje){
        String valor = "";
        boolean valido = false;

        do{
            System.out.print(mensaje);
            valor = sc.nextLine();

            if(valor.trim().isEmpty()){
                System.out.println("No puedes dejar espacios vacios");
                
            }else if(valor.trim().length() > 30){
                System.out.println("Tiene maximo 30 letras");
                
            }else{
                valido = true;
            }
        }
        while(!valido);

        return valor.trim();
    }

    public static double leerMonto(Scanner sc, String mensaje){
        double monto = 0.0;
        boolean valido = false;

        do{
            System.out.print(mensaje);
            String entrada = sc.nextLine();

            try{
                monto = Double.parseDouble(entrada.trim());

                if(monto < 0){
                    System.out.println("El monto no puede ser negativo");
                    
                }else{
                    valido = true;
                }
            }
            catch(NumberFormatException e){
                System.out.println("Ingrese un valor numerico valido");
            }
        }
        while(!valido);

        return monto;
    }

    public static int leerOpcion(Scanner sc, int min, int max){
        int opcion = 0;
        boolean valido = false;

        do{
            System.out.print("Selecione una opcion: ");
            String entrada = sc.nextLine();

            try{
                opcion = Integer.parseInt(entrada.trim());

                if(opcion < min || opcion > max){
                    System.out.println("Ingrese entre " + min + " y " + max);
                    
                }else{
                    valido = true;
                }
            }
            catch(NumberFormatException e){
                System.out.println("Ingrese un numero valido");
            }
        }
        while(!valido);

        return opcion;
    }

    public static char leerSN(Scanner sc, String mensaje){
        char respuesta = ' ';
        boolean valido = false;

        do{
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim().toUpperCase();

            if(entrada.equals("S") || entrada.equals("N")){
                respuesta = entrada.charAt(0);
                valido = true;
                
            }else{
                System.out.println("Ingrese S o N");
            }
        }
        while(!valido);

        return respuesta;
    }

}