package com.fanda

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

// 自定义 Task 实现 propertiesVersion.gradle 的 makeReleaseVersion 任务的功能
class ReleaseVersionTask extends DefaultTask {
    // release 和 destFile 配置属性将暴露给构建脚本单独设置
    // 通过注解来表示输入数据
    @Input
    Boolean release
    // 通过注解来表示输出数据
    @OutputFile
    File destFile

    ReleaseVersionTask() {
        // 在构造器中设置分组和描述
        group('versioning')
        description('Makes project a release version.')
    }

    // 通过注解来定义 action
    @TaskAction
    void start() {
        project.version.release = release
        ant.propertyfile(file: destFile) {
            entry(key: 'release', type: 'string', operation: '=', value: 'true')
        }
    }
}