/**
 * thermalRes - The containers Thermal Resistance (for each material)
 * thickness - The containers Thickness for each material
 * length - The container's Length
 * width - The container's Width
 * height - The container's Height
 * ID - The container's ID
 * X  - The container position in the X axis
 * Y  - The container position in the Y axis
 * Z  - The container position in the Z axis
 * Refrigerated - A flag to see if the container is refrigerated, 1 if it is 0 if it isn't
 */
typedef struct {
    float thermalRes[3];
    float thickness[3];
    float length;
    float width;
    float height;
    int id;
    char refrigerated;
    unsigned char x;
    unsigned char y;
    unsigned char z;
} container;
