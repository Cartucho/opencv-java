if(NOT ANT_EXECUTABLE)
  return()
endif()

message("heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeyyyyyyyyyyy")

if(BUILD_EXAMPLES)

  project(java_samples)

  # ---------------------------------------------
  #      Define executable targets
  # ---------------------------------------------
  MACRO(OPENCV_DEFINE_JAVA_EXAMPLE name srcs)

      message("yi2")
      if("${srcs}" MATCHES "tutorial_code")
        set(sample_kind tutorial)
        set(sample_KIND TUTORIAL)
        set(sample_subfolder "tutorials")
        message("here")
      endif()
      message("ended----------")

  ENDMACRO()

  file(GLOB_RECURSE java_samples RELATIVE ${CMAKE_CURRENT_SOURCE_DIR} *.java)

  foreach(sample_filename ${java_samples})
      message(${sample_filename})
      get_filename_component(sample ${sample_filename} NAME_WE)
      OPENCV_DEFINE_JAVA_EXAMPLE(${sample}  ${sample_filename})
  endforeach()

  # Copy the OpenCV jar after it has been generated.
  add_custom_command(OUTPUT "${opencv_test_java_bin_dir}/bin/${JAR_NAME}"
                     COMMAND ${CMAKE_COMMAND} -E copy_if_different "${JAR_FILE}" "${opencv_test_java_bin_dir}/bin/${JAR_NAME}"
                     DEPENDS "${JAR_FILE}"
                     COMMENT "Copying the OpenCV jar"
                    )

  add_custom_command(OUTPUT "${opencv_test_java_bin_dir}/build.xml"
                     COMMAND ${CMAKE_COMMAND} -E copy_if_different "${CMAKE_CURRENT_SOURCE_DIR}/build.xml" "${opencv_test_java_bin_dir}/build.xml"
                     DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/build.xml"
                     COMMENT "Copying build.xml"
                    )
endif()
