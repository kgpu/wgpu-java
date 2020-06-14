package com.noahcharlton.wgpuj.jnrgen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class EnumItem implements Item {

    private final String name;
    private final List<EnumField> fields;

    public EnumItem(String name, List<EnumField> fields) {
        this.name = name.replace("_", "");
        this.fields = fields;

        this.fields.sort(Comparator.comparingInt(field -> field.index));
    }

    @Override
    public void save(Config config) throws IOException {
        String className = this.name.replace("WGPU", "Wgpu");

        BufferedWriter writer = config.startFile(className + ".java");

        writer.write("public enum ");
        writer.write(className.replace("WGPU", "Wgpu"));
        writer.write(" {\n");

        saveFields(writer);

        writer.write("}");

        writer.flush();
        writer.close();
    }

    private void saveFields(BufferedWriter writer) throws IOException {
        for(EnumField field : fields){
            writer.write("    ");
            writer.write(toFieldName(field.name));
            writer.write(",\n");
        }
    }

    private String toFieldName(String fieldName) {
        //Replace tag due to CBindgen adding "Tag" for enum names, but not for each field for some reason
        String cFieldHeader = this.name.replace("Tag", "");

        fieldName = fieldName.replace(cFieldHeader, "").replace("_", "");
        StringBuilder output = new StringBuilder();

        for(char c : fieldName.toCharArray()){
            if(Character.isUpperCase(c) && !output.toString().isEmpty())
                output.append("_");

            output.append(Character.toUpperCase(c));
        }

        return output.toString();
    }

    static class EnumField{
        private final String name;
        private final int index;

        public EnumField(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }
}
