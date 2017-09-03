package codeGeneration;

import java.io.File;

public interface CodeGeneratorCallback {

	public File showSaveDialog();
	public void onGenerationComplete(String string);
}
