package com.huang.auto.coder.mybatis.service;

import com.huang.auto.coder.mybatis.swing.MethodEnum;
import com.huang.auto.coder.utils.JavaClassTransverter;
import com.huang.auto.coder.utils.StringTransverter;
import com.huang.auto.coder.utils.Table;

import java.io.File;
import java.util.*;

/**
 * Created by JianQiu on 2016/11/2.
 */
public class ServiceBuildFactory extends MyBatisCodeBuildFactory {

    private File mapperSaveDirectory;
    private String mapperInterface;

    public ServiceBuildFactory(File saveDirectory, String interfaceName, File mapperSaveDirectory
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

        String import_bean = JavaClassTransverter.builderImportByFile(beanFile);
        String packageMessage = JavaClassTransverter.builderPackageByFile(saveDirectory);
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
        String beanClassName = JavaClassTransverter.getClassName(beanFile);
        String beanClassLowerName = StringTransverter.initialLowerCaseTransvert(beanClassName);
        for (MethodEnum methodEnum : methodEnumSet) {
            List<MethodInfo> methodInfoList = methodInfoListMap.get(methodEnum);
            String resultContext;
            switch (methodEnum) {
                case SELECT:
                    resultContext = "List<" + beanClassName + ">";
                    break;
                default:
                    resultContext = "void";
                    break;
            }
            for (MethodInfo methodInfo : methodInfoList) {
                if (methodEnum == MethodEnum.SELECT &&
                        (methodInfo.getWhereColumnList() == null || methodInfo.getWhereColumnList().size() == 0)) {
                    methodContext.append("\tpublic " + resultContext + " " + methodInfo.getMethodName() + "();\n");
                } else {
                    methodContext.append("\tpublic " + resultContext + " " + methodInfo.getMethodName()
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
        String import_bean = JavaClassTransverter.builderImportByFile(beanFile);
        String import_mapper = "import " + JavaClassTransverter.builderPackageContextByFile(mapperSaveDirectory)
                + "." + mapperInterface + ";\n";
        String packageMessage = JavaClassTransverter.builderPackageByFile(saveDirectory);

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
        String beanClassName = JavaClassTransverter.getClassName(beanFile);
        String beanClassLowerName = StringTransverter.initialLowerCaseTransvert(beanClassName);
        String mapperInterfaceLowerName = StringTransverter.initialLowerCaseTransvert(mapperInterface);
        for (MethodEnum methodEnum : methodEnumSet) {
            List<MethodInfo> methodInfoList = methodInfoListMap.get(methodEnum);
            String resultContext;
            switch (methodEnum) {
                case SELECT:
                    resultContext = "List<" + beanClassName + ">";
                    break;
                default:
                    resultContext = "void";
                    break;
            }
            for (MethodInfo methodInfo : methodInfoList) {
                methodImplContext.append("\t@Override\n");

                if (methodEnum == MethodEnum.SELECT) {
                    if (methodInfo.getWhereColumnList() == null || methodInfo.getWhereColumnList().size() == 0) {
                        /* public List<Pojo> loadAllRecordBuffered() {
                         *    return recordBufferedMapper.loadAllRecordBuffered();
                         */
                        methodImplContext.append("\tpublic " + resultContext + " " + methodInfo.getMethodName()
                                + "(){\n");
                        methodImplContext.append("\t\treturn " + mapperInterfaceLowerName + "."
                                + methodInfo.getMethodName() + "();\n");
                    } else {
                        /* public List<Pojo> loadAllRecordBuffered(Pojo pojo) {
                         *    return recordBufferedMapper.loadAllRecordBuffered(Pojo pojo);
                         */
                        methodImplContext.append("\tpublic " + resultContext + " " + methodInfo.getMethodName()
                                + "(" + beanClassName + " " + beanClassLowerName + "){\n");
                        methodImplContext.append("\t\treturn " + mapperInterfaceLowerName + "." + methodInfo.getMethodName()
                                + "(" + beanClassLowerName + ");\n");
                    }
                } else {
                     /* public void loadAllRecordBuffered(Pojo pojo) {
                      *    recordBufferedMapper.loadAllRecordBuffered(Pojo pojo);
                      */
                    methodImplContext.append("\tpublic " + resultContext + " " + methodInfo.getMethodName()
                            + "(" + beanClassName + " " + beanClassLowerName + "){\n");
                    methodImplContext.append("\t\t" + mapperInterfaceLowerName + "." + methodInfo.getMethodName() + "("
                            + beanClassLowerName + ");\n");
                }
                methodImplContext.append("\t}\n");
            }
            methodImplContext.append("\n");
        }
        return methodImplContext;
    }
}
