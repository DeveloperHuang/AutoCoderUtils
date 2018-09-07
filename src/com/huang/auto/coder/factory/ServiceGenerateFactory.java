package com.huang.auto.coder.factory;

import com.huang.auto.coder.factory.pojo.Column;
import com.huang.auto.coder.factory.pojo.Method;
import com.huang.auto.coder.swing.mybatis.MethodEnum;
import com.huang.auto.coder.utils.StringTransverter;
import com.huang.auto.coder.factory.pojo.Table;

import java.io.File;
import java.util.*;

/**
 * Created by Joss on 2016/11/2.
 */
public class ServiceGenerateFactory extends ContextGenerateFactory {

    private File mapperSaveDirectory;
    private String mapperInterface;

    public ServiceGenerateFactory(File saveDirectory, String interfaceName, File mapperSaveDirectory
            , String mapperInterface, File beanFile, Table beanTable) {
        super(saveDirectory, interfaceName, beanFile, beanTable);
        this.mapperSaveDirectory = mapperSaveDirectory;
        this.mapperInterface = mapperInterface;
    }

    /**
     * 获取接口内容
     *
     * @return 内容字符串
     */
    @Override
    public String getInterfaceContext() {
        StringBuffer interfaceContextBuffer = new StringBuffer();

        String import_bean = JavaClassContextGenerator.generateImportByFile(beanFile);
        String packageMessage = JavaClassContextGenerator.generatePackageByFile(saveDirectory);
        String head = packageMessage + "\n\n" + IMPORT_LIST + "\n" + import_bean + "\n\n";
        interfaceContextBuffer.append(head);
        interfaceContextBuffer.append("public interface " + interfaceName + " {\n\n");
        interfaceContextBuffer.append(getInterfaceMethodContext());
        interfaceContextBuffer.append("}");

        return interfaceContextBuffer.toString();
    }

    /**
     * 获取实现的内容
     *
     * @return 内容字符串
     */
    @Override
    public String getImplementsContext() {

        StringBuffer implementsContext = new StringBuffer();
        String import_bean = JavaClassContextGenerator.generateImportByFile(beanFile);
        String import_mapper = "import " + JavaClassContextGenerator.generatePackageUrlByFile(mapperSaveDirectory)
                + "." + mapperInterface + ";\n";
        String packageMessage = JavaClassContextGenerator.generatePackageByFile(saveDirectory);

        String head = packageMessage + "\n\n" + IMPORT_LIST + "\n" +IMPORT_SERVICE+"\n"+IMPORT_AUTOWIRED+"\n"
                + import_mapper + import_bean + "\n\n";

        String mapperParameter = "\t@Autowired\n\tprivate " + mapperInterface + " "
                + StringTransverter.initialLowerCaseTransvert(mapperInterface) + ";\n";

        implementsContext.append(head);
        implementsContext.append("@Service(\""+StringTransverter.initialLowerCaseTransvert(interfaceName)+"\")\n");
        implementsContext.append("public class " + interfaceName + "Impl implements " + interfaceName + "{\n\n");
        implementsContext.append(mapperParameter);
        implementsContext.append("\n");
        implementsContext.append(getMethodImplContext());
        implementsContext.append("}");
        return implementsContext.toString();
    }


    /**
     * 获取方向实现内容
     */
    private StringBuffer getMethodImplContext() {
        StringBuffer methodImplContext = new StringBuffer();
        Set<MethodEnum> methodEnumSet = methodInfoListMap.keySet();
        String beanClassName = JavaClassContextGenerator.getClassName(beanFile);
        String beanClassLowerName = StringTransverter.initialLowerCaseTransvert(beanClassName);
        String mapperInterfaceLowerName = StringTransverter.initialLowerCaseTransvert(mapperInterface);
        for (MethodEnum methodEnum : methodEnumSet) {
            List<Method> methodList = methodInfoListMap.get(methodEnum);
            String returnBeanClass;
            switch (methodEnum) {
                case SELECT:
                    returnBeanClass = "List<" + beanClassName + ">";
                    break;
                default:
                    returnBeanClass = "void";
                    break;
            }
            for (Method method : methodList) {
                methodImplContext.append("\t@Override\n");

                if (methodEnum == MethodEnum.SELECT) {
                    if (method.getWhereColumnList() == null || method.getWhereColumnList().size() == 0) {
                        /* public List<Pojo> loadAllRecordBuffered() {
                         *    return recordBufferedMapper.loadAllRecordBuffered();
                         */
                        methodImplContext.append("\tpublic " + returnBeanClass + " " + method.getMethodName()
                                + "(){\n");
                        methodImplContext.append("\t\treturn " + mapperInterfaceLowerName + "."
                                + method.getMethodName() + "();\n");
                    } else {
                        /* public Pojo getById(Integer id) {
                         *    return recordBufferedMapper.getById(Integer id);
                         *    }
                         */
                        //如果只包含一个条件参数，且该条件参数是主键，则返回结果为对象，不为list
                        if(method.getWhereColumnList().size() == 1
                                && method.getWhereColumnList().get(0).isPrimaryKey()){
                            Column column = method.getWhereColumnList().get(0);
                            String fieldType = column.getFieldType();
                            String fieldName = column.getFieldName();
                            //WHERE 条件使用小驼峰命名法获取参数
                            String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                            String javaType = JavaBeanFactory.getJavaTypeByDataBaseType(fieldType);
                            methodImplContext.append("\tpublic " + beanClassName + " " + method.getMethodName()
                                    + "(" + javaType + " " + lowerCamelFieldName + "){\n");
                            methodImplContext.append("\t\treturn " + mapperInterfaceLowerName + "." + method.getMethodName()
                                    + "(" + lowerCamelFieldName + ");\n");
                        }else{
                            /* public List<Pojo> loadAllRecordBuffered(Pojo pojo) {
                         *      return recordBufferedMapper.loadAllRecordBuffered(Pojo pojo);
                         *      }
                         */
                            methodImplContext.append("\tpublic " + returnBeanClass + " " + method.getMethodName()
                                    + "(" + beanClassName + " " + beanClassLowerName + "){\n");
                            methodImplContext.append("\t\treturn " + mapperInterfaceLowerName + "." + method.getMethodName()
                                    + "(" + beanClassLowerName + ");\n");
                        }
                    }
                } else if(methodEnum == MethodEnum.DELETE){
                    if(method.getWhereColumnList().size() == 1
                            && method.getWhereColumnList().get(0).isPrimaryKey()){
                        Column column = method.getWhereColumnList().get(0);
                        String fieldType = column.getFieldType();
                        String fieldName = column.getFieldName();
                        //WHERE 条件使用小驼峰命名法获取参数
                        String lowerCamelFieldName = StringTransverter.lowerCamelCase(fieldName);
                        String javaType = JavaBeanFactory.getJavaTypeByDataBaseType(fieldType);
                         /* public void deleteById(Integer id) {
                      *         recordBufferedMapper.deleteById(id)
                      *    }
                      */
                        methodImplContext.append("\tpublic " + returnBeanClass + " " + method.getMethodName()
                                + "(" + javaType + " " + lowerCamelFieldName + "){\n");
                        methodImplContext.append("\t\t" + mapperInterfaceLowerName + "." + method.getMethodName()
                                + "(" + lowerCamelFieldName + ");\n");
                    }else{
                        methodImplContext.append("\tpublic " + returnBeanClass + " " + method.getMethodName()
                                + "(" + beanClassName + " " + beanClassLowerName + "){\n");
                        methodImplContext.append("\t\t" + mapperInterfaceLowerName + "." + method.getMethodName() + "("
                                + beanClassLowerName + ");\n");
                    }
                }else {
                     /* public void loadAllRecordBuffered(Pojo pojo) {
                      *    recordBufferedMapper.loadAllRecordBuffered(Pojo pojo);
                      */
                    methodImplContext.append("\tpublic " + returnBeanClass + " " + method.getMethodName()
                            + "(" + beanClassName + " " + beanClassLowerName + "){\n");
                    methodImplContext.append("\t\t" + mapperInterfaceLowerName + "." + method.getMethodName() + "("
                            + beanClassLowerName + ");\n");
                }
                methodImplContext.append("\t}\n");
            }
            methodImplContext.append("\n");
        }
        return methodImplContext;
    }
}
