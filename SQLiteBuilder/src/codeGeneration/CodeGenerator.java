package codeGeneration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import models.Database;
import models.SQLTableColumn;
import models.Table;

public class CodeGenerator {

    private Database db;
    private CodeGeneratorCallback caller;

    File dbFile;
    File dbHelper;

    public CodeGenerator(Database db, CodeGeneratorCallback caller) {
        this.db = db;
        this.caller = caller;
    }

    public void generateCode() {

        File loc = caller.showSaveDialog();
        if (loc == null) {
            return;
        }
        System.out.println(loc.getAbsolutePath());

        try {

            loc.mkdir();

            dbFile = new File(loc + "/Database.java");
            dbFile.createNewFile();

            writeCodeToDatabaseFile();

            dbHelper = new File(loc + "/DBHelper.java");
            dbHelper.createNewFile();

            writeCodeToHelperFile();

            caller.onGenerationComplete(loc.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeCodeToDatabaseFile() throws IOException {

        FileOutputStream fos = new FileOutputStream(dbFile);

        writeStdOpeningCode(fos);

        writeModelAdditionCode(fos);

        writeClosingCode(fos);

        fos.flush();
        fos.close();

    }

    private void writeStdOpeningCode(FileOutputStream fos) throws IOException {
        String code = getFile("DatabaseOpening.txt");

        fos.write(code.getBytes());

    }

    private void writeModelAdditionCode(FileOutputStream fos) throws IOException {
        for (Table model : db.getTables()) {
            String name = model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1);
            StringBuilder inputBuilder = new StringBuilder();
            for (SQLTableColumn col : model.getCols()) {
                if (!col.getName().equals("ID")) {
                    inputBuilder.append("," + col.getType().getJavaType() + " " + col.getName());
                }
            }

            String header;
            if (inputBuilder.toString().length() > 1) {
                header = "\tpublic long add" + name + "(" + inputBuilder.toString().substring(1) + "){\n";
            } else {
                header = "\tpublic long add" + name + "(){\n";
            }

            StringBuilder body = new StringBuilder();

            body.append("\t\tContentValues cv = new ContentValues();\n");
            for(SQLTableColumn col : model.getCols()) {
                if (!col.getName().equals("ID")) {
                    body.append("\t\tcv.put(DBHelper.").append(model.getName().toUpperCase()).append("_").append(col.getName().toUpperCase()).append(", ").append(col.getName()).append(");\n");
                }
            }
            body.append("\t\treturn database.insert(DBHelper.TABLE_" + model.getName().toUpperCase() + ", null, cv);");

            String close = "\n\t}\n\n";

            fos.write(header.getBytes());
            fos.write(body.toString().getBytes());
            fos.write(close.getBytes());

        }

    }

    private void writeClosingCode(FileOutputStream fos) throws IOException {
        String code = "\n}";
        fos.write(code.getBytes());
    }

    private void writeCodeToHelperFile() throws IOException {
        FileOutputStream fos = new FileOutputStream(dbHelper);

        writeHelperOpening(fos);
        writeHelperVars(fos);
        writeHelperConstructor(fos);
        writeHelperOnCreate(fos);
        writeHelperClosing(fos);

        fos.flush();
        fos.close();

    }

    private void writeHelperOpening(FileOutputStream fos) throws IOException {

        // write imports
        fos.write("import android.content.Context;\n".getBytes());
        fos.write("import android.database.sqlite.SQLiteDatabase;\n".getBytes());
        fos.write("import android.database.sqlite.SQLiteOpenHelper;\n".getBytes());

        // write class header
        fos.write("public class DBHelper extends SQLiteOpenHelper {\n".getBytes());
    }

    private void writeHelperVars(FileOutputStream fos) throws IOException {
        fos.write("\n\t//database info\n".getBytes());
        // write db name var
        fos.write("\tpublic static final String DATABASE_NAME=\"".getBytes());
        fos.write(db.getName().getBytes());
        fos.write("\";\n".getBytes());
        // write db version var
        fos.write("\tpublic static final int DATABASE_VERSION=1;\n".getBytes());
        // write db tables
        fos.write("\n\t//database tables".getBytes());

        for (Table model : db.getTables()) {
            String modelUpperName = model.getName().toUpperCase();
            // write model comment
            String tableLeadingComment = "\n\t//" + model.getName() + " table keys\n";
            fos.write(tableLeadingComment.getBytes());
            // write table name var
            String modelTable = "\tpublic static final String TABLE_" + modelUpperName + "=\""
                    + model.getName().toLowerCase() + "_table\";";
            fos.write(modelTable.getBytes());

            // write col vars
            for (SQLTableColumn col : model.getCols()) {
                String colVar = "\n\tpublic static final String " + modelUpperName + "_" + col.getName().toUpperCase()
                        + "=\"" + model.getName() + "_" + col.getName() + "\";";
                fos.write(colVar.getBytes());
            }
            fos.write("\n".getBytes());

        }

        fos.write("".getBytes());
        fos.write("".getBytes());
        fos.write("".getBytes());
        fos.write("".getBytes());

    }

    private void writeHelperConstructor(FileOutputStream fos) throws IOException {
        fos.write("\n\tpublic DBHelper(Context context) {\n".getBytes());
        fos.write("\t\t super(context, DATABASE_NAME, null, DATABASE_VERSION);\n".getBytes());
        fos.write("\t}\n\n".getBytes());
    }

    private void writeHelperOnCreate(FileOutputStream fos) throws IOException {

        fos.write("\t@Override\n".getBytes());
        fos.write("\tpublic void onCreate(SQLiteDatabase db) {\n".getBytes());
        fos.write("\t\t//create the tables\n".getBytes());

        for (Table model : db.getTables()) {
            String nameUpper = model.getName().toUpperCase();
            StringBuilder code = new StringBuilder(
                    "\t\tdb.execSQL(\"CREATE TABLE \" + TABLE_" + nameUpper + " +\" (\"+\n");
            for (SQLTableColumn col : model.getCols()) {
                code.append("\t\t\t\t" + nameUpper + "_" + col.getName().toUpperCase() + " + ");
                if (col.getName().equals("ID")) {
                    code.append("\" INTEGER PRIMARY KEY AUTOINCREMENT, \"+\n");
                } else {
                    code.append("\" " + col.getType().getName());
                    if (col.isNotNull()) {
                        code.append(" NOT NULL");
                    }
                    code.append(",\" + \n");
                }
            }

            String finalCode = code.toString().substring(0, code.toString().length() - 6) + "\" + \n";
            fos.write(finalCode.getBytes());
            fos.write("\t\t\t\t\");\"\n".getBytes());
            fos.write("\t\t);\n\n".getBytes());

        }
        fos.write("\t}\n".getBytes());

        fos.write("".getBytes());

    }

    private void writeHelperClosing(FileOutputStream fos) throws IOException {

        // write onUpgrade function
        fos.write("\n\t@Override\n".getBytes());
        fos.write("\tpublic void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {\n".getBytes());
        fos.write("\t\t//drop all existing tables\n".getBytes());

        for (Table model : db.getTables()) {
            String dropCode = "\t\tdb.execSQL(\"DROP TABLE IF EXISTS \"+TABLE_" + model.getName().toUpperCase()
                    + ");\n";
            fos.write(dropCode.getBytes());
        }

        fos.write("\t\tonCreate(db);\n\t}".getBytes());
        fos.write("\n}".getBytes());
    }

    private String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");
        // Get file from resources folder
        // System.out.println(this.getClass().getResource("DatabaseOpening.txt"));
        //File file = new File("src/resources/" + fileName);
        InputStream file = getClass().getResourceAsStream(fileName);

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();

    }

}
