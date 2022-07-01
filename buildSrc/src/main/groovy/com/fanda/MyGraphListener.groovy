// 记得加上这行包名，不然构建脚本导包不了
package com.fanda

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

/* Task Graph 生成的监听实现，注意这种方式和在脚本中通过闭包的方式的实现区别，在这里是不能直接访问 project 实例的*/

class MyGraphListener implements TaskExecutionGraphListener {
    // 注意这个任务名称，前面是跟模块名称的，不能直接写 'release'
    final static String releaseTaskPath = ':todo:release'

    // 当 Task Graph 生成，在 action 执行之前会被回调
    @Override
    void graphPopulated(TaskExecutionGraph taskGraph) {

        // 如果 task 图中包含 release 任务
        List<Task> allTasks = taskGraph.allTasks
        if (taskGraph.hasTask(releaseTaskPath)) {
            // 找到 release 任务
            Task releaseTask = allTasks.find { it.path == releaseTaskPath }
            // 找到 release 任务所属的 Project 对象
            Project project = releaseTask.project
            if (!project.version.release) {
                project.version.release = true
                project.ant.propertyfile(file: project.versionFile) {
                    entry(key: 'release', type: 'string', operation: '=', value: 'true')
                }
            }
        }
    }
}