cmake_minimum_required(VERSION 3.10.2)

project(snes9x)

# Find all source files
file(GLOB_RECURSE SOURCES "*.cpp" "*.h")

# Exclude included files that are not standalone
list(FILTER SOURCES EXCLUDE REGEX "oppseudo")

# Add the shared library
add_library(snes9x SHARED ${SOURCES})

# Include directories
include_directories(.)

# Link necessary libraries (e.g., log for Android)
find_library(log-lib log)
target_link_libraries(snes9x ${log-lib})

# Set C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)