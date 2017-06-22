# ----------------------------------------------------------------------------
#  CMake file for Java tutorials.
#
# ----------------------------------------------------------------------------

if(NOT ANT_EXECUTABLE
  OR NOT BUILD_JAVA_EXAMPLES)
  return()
endif()

project(java_tutorials)

set(opencv_tutorial_java_bin_dir "${CMAKE_CURRENT_BINARY_DIR}/.build")

# make sure the build directory exists
file(MAKE_DIRECTORY "${opencv_tutorial_java_bin_dir}")

# Copy the OpenCV jar after it has been generated.
add_custom_command(OUTPUT "${opencv_tutorial_java_bin_dir}/bin/${JAR_NAME}"
                   COMMAND ${CMAKE_COMMAND} -E copy_if_different "${JAR_FILE}" "${opencv_tutorial_java_bin_dir}/bin/${JAR_NAME}"
                   DEPENDS "${JAR_FILE}"
                   COMMENT "Copying the OpenCV jar"
)

file(GLOB_RECURSE java_tutorials RELATIVE ${CMAKE_CURRENT_SOURCE_DIR} tutorial_code/*.java)

foreach(tutorial_filename ${java_tutorials})
  get_filename_component(tutorial ${tutorial_filename} NAME_WE)
  message(${tutorial_filename})
endforeach()
