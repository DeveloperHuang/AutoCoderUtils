package com.huang.auto.coder.factory;

import com.huang.auto.coder.factory.pojo.Method;
import com.huang.auto.coder.swing.mybatis.MethodEnum;
import com.huang.auto.coder.utils.StringTransverter;
import com.huang.auto.coder.factory.pojo.Table;

import java.io.File;
import java.util.*;

/**
 * Created by JianQiu on 2016/11/2.
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
     * 获取接口方法内容
     *
     * @return 方法内容字符串
     */
    private StringBuffer getInterfaceMethodContext() {
        StringBuffer methodContext = new StringBuffer();
        Set<MethodEnum> methodEnumSet = methodInfoListMap.keySet();
        String beanClassName = JavaClassContextGenerator.getClassName(beanFile);
        String beanClassLowerName = StringTransverter.initialLowerCaseTransvert(beanClassName);
        for (MethodEnum methodEnum : methodEnumSet) {
            List<Method> methodList = methodInfoListMap.get(methodEnum);
            String resultContext;
            switch (methodEnum) {
                case SELECT:
                    resultContext = "List<" + beanClassName + ">";
                    break;
                default:
                    resultContext = "void";
                    break;
            }
            for (Method method : methodList) {
                if (methodEnum == MethodEnum.SELECT &&
                        (method.getWhereColumnList() == null || method.getWhereColumnList().size() == 0)) {
                    methodContext.append("\tpublic " + resultContext + " " + method.getMethodName() + "();\n");
                } else {
                    methodContext.append("\tpublic " + resultContext + " " + method.getMethodName()
                            + "(" + beanClassName + " " + beanClassLowerName + ");\n");
                }
            }
            methodContext.append("\n");
        }
        return methodContext;
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
        String import_mapper = "import " + JavaClassContextGenerator.generatePackageContextByFile(mapperSaveDirectory)
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
            String resultContext;
            switch (methodEnum) {
                case SELECT:
                    resultContext = "List<" + beanClassName + ">";
                    break;
                default:
                    resultContext = "void";
                    break;
            }
            for (Method method : methodList) {
                methodImplContext.append("\t@Override\n");

                if (methodEnum == MethodEnum.SELECT) {
                    if (method.getWhereColumnList() == null || method.getWhereColumnList().size() == 0) {
                        /* public List<Pojo> loadAllRecordBuffered() {
                         *    return recordBufferedMapper.loadAllRecordBuffered();
                         */
                        methodImplContext.append("\tpublic " + resultContext + " " + method.getMethodName()
                                + "(){\n");
                        methodImplContext.append("\t\treturn " + mapperInterfaceLowerName + "."
                                + method.getMethodName() + "();\n");
                    } else {
                        /* public List<Pojo> loadAllRecordBuffered(Pojo pojo) {
                         *    return recordBufferedMapper.loadAllRecordBuffered(Pojo pojo);
                         */
                        methodImplContext.append("\tpublic " + resultContext + " " + method.getMethodName()
                                + "(" + beanClassName + " " + beanClassLowerName + "){\n");
                        methodImplContext.append("\t\treturn " + mapperInterfaceLowerName + "." + method.getMethodName()
                                + "(" + beanClassLowerName + ");\n");
                    }
                } else {
                     /* public void loadAllRecordBuffered(Pojo pojo) {
                      *    recordBufferedMapper.loadAllRecordBuffered(Pojo pojo);
                      */
                    methodImplContext.append("\tpublic " + resultContext + " " + method.getMethodName()
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
