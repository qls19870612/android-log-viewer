/*
 * Copyright 2018 Mikhail Lopatkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitbucket.mlopatkin.android.logviewer.building

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import javax.lang.model.element.Modifier

/**
 * A task to generate build metadata java class.
 */
class GenerateBuildMetadata extends DefaultTask {
    @Input
    String packageName

    @Input
    String className

    @Input
    String version

    @OutputDirectory
    File into

    @TaskAction
    def generate(IncrementalTaskInputs inputs) {
        def buildEnv = new BuildEnvironment(project.projectDir, 'hg')
        def fullClassName = ClassName.get(packageName, className)
        def metadataClass = TypeSpec.classBuilder(fullClassName).addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addField(stringConstant('VERSION', version, 'Version of the application.'))
                .addField(stringConstant('BUILD', buildEnv.buildNumber, 'Build number if the app is built on CI.'))
                .addField(stringConstant('REVISION', buildEnv.sourceRevision, 'Source revision of which app is built.'))
                .build()
        JavaFile.builder(fullClassName.packageName(), metadataClass)
                .indent('    ')
                .skipJavaLangImports(true)
                .addFileComment('This file was generated by GenerateBuildMetadata task.\nDO NOT EDIT!')
                .build().writeTo(into)
    }

    private static def stringConstant(String name, String value, String doc) {
        def field = FieldSpec.builder(String.class, name, Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                .initializer('$S', value)
                .addJavadoc('$L\n', doc)
                .build()
        return field
    }
}
