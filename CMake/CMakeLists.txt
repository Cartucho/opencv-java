# ----------------------------------------------------------------------------
#  CMake file for Java tutorials compilation.
#
# ----------------------------------------------------------------------------
if(NOT ANT_EXECUTABLE)
  return()
endif()

project(compile_java_tutorials)

MACRO(SUBSUBDIRLIST result curdir)
  FILE(GLOB children RELATIVE ${curdir} ${curdir}/*/*)
  SET(dirlist "")
  FOREACH(child ${children})
    IF(IS_DIRECTORY ${curdir}/${child})
      FILE(GLOB contains_java_files "${child}/*.java")
      if(contains_java_files)
        LIST(APPEND dirlist ${child})
      endif()
    ENDIF()
  ENDFOREACH()
  SET(${result} ${dirlist})
ENDMACRO()

SUBSUBDIRLIST(TUTORIALS ${CMAKE_CURRENT_SOURCE_DIR})

set(opencv_tutorial_java_bin_dir "${CMAKE_CURRENT_BINARY_DIR}/.compiled")
set(target_name "compile_java_tutorials")

add_custom_target("${target_name}" ALL 
                  DEPENDS opencv_java
                 )

FOREACH(tutorial_dir ${TUTORIALS})
  get_filename_component(tutorial_name ${tutorial_dir} NAME_WE)
  add_custom_command(TARGET "${target_name}" 
                     COMMAND ${ANT_EXECUTABLE}
                          -DocvJarDir="${OpenCV_BINARY_DIR}/bin"
                          -DsrcDir="${tutorial_dir}"
                          -DdstDir="${opencv_tutorial_java_bin_dir}/${tutorial_name}"
                     WORKING_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}"
                     COMMENT "Compile the tutorial: ${tutorial_name}"
                    )
ENDFOREACH()
