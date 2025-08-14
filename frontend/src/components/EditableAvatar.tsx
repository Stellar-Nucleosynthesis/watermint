import { Button, Text, Stack, Group, Modal, Box, Slider, Avatar } from "@mantine/core";
import Cropper from "react-easy-crop";
import { notifications } from '@mantine/notifications';
import { IconX } from '@tabler/icons-react';
import { useState, useCallback, useRef } from "react";
import { base64ToMimeDataUrl, mimeDataUrlToBase64 } from "../utils/base64Utils";

const createImage = (url: string): Promise<HTMLImageElement> =>
    new Promise((resolve, reject) => {
        const image = new Image();
        image.addEventListener("load", () => resolve(image));
        image.addEventListener("error", (e) => reject(e));
        image.setAttribute("crossOrigin", "anonymous");
        image.src = url;
    });

async function getCroppedImg(imageSrc: string, pixelCrop: { x: number; y: number; width: number; height: number; }) {
    const image = await createImage(imageSrc);
    const canvas = document.createElement("canvas");

    canvas.width = pixelCrop.width;
    canvas.height = pixelCrop.height;

    const ctx = canvas.getContext("2d");
    if (!ctx) throw new Error("Failed to get canvas context");

    ctx.drawImage(
        image,
        pixelCrop.x,
        pixelCrop.y,
        pixelCrop.width,
        pixelCrop.height,
        0,
        0,
        pixelCrop.width,
        pixelCrop.height
    );

    return new Promise<Blob | null>((resolve) => {
        canvas.toBlob((blob) => resolve(blob), "image/jpeg", 0.92);
    });
}

interface EditableAvatarProps {
    picture: string | undefined,
    setPicture: (picture: string | undefined) => void
}

function EditableAvatar({ picture, setPicture }: EditableAvatarProps) {
    const [pictureDataUrl, setPictureDataUlr] = useState(base64ToMimeDataUrl(picture));
    const [cropModalOpen, setCropModalOpen] = useState(false);
    const [selectedImage, setSelectedImage] = useState<string | null>(null);
    const [crop, setCrop] = useState({ x: 0, y: 0 });
    const [zoom, setZoom] = useState(1);
    const [croppedAreaPixels, setCroppedAreaPixels] = useState<{ x: number; y: number; width: number; height: number; } | null>(null);

    const xIcon = <IconX size={20} />;

    const fileInputRef = useRef<HTMLInputElement | null>(null);

    const onAvatarClick = () => fileInputRef.current?.click();

    const onFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files && e.target.files[0];
        if (!file)
            return;
        const reader = new FileReader();
        reader.onload = () => {
            setSelectedImage(reader.result as string);
            setCropModalOpen(true);
        };
        reader.readAsDataURL(file);
        e.currentTarget.value = "";
    };

    const onCropComplete = useCallback((_: any, croppedAreaPixelsParam: any) => {
        setCroppedAreaPixels(croppedAreaPixelsParam);
    }, []);

    const handleApplyCrop = async () => {
        if (!selectedImage || !croppedAreaPixels)
            return;
        try {
            const blob = await getCroppedImg(selectedImage, croppedAreaPixels);
            if (!blob)
                throw new Error("Failed to create cropped image");

            const mimeDataUrl = await new Promise<string>((resolve, reject) => {
                const reader = new FileReader();
                reader.onloadend = () => resolve(reader.result as string);
                reader.onerror = reject;
                reader.readAsDataURL(blob);
            });

            setPictureDataUlr(mimeDataUrl);
            setPicture(mimeDataUrlToBase64(mimeDataUrl));

            setCropModalOpen(false);
            setSelectedImage(null);
            setCrop({ x: 0, y: 0 });
            setZoom(1);
            setCroppedAreaPixels(null);
        } catch (e) {
            console.error(e);
            notifications.show({
                title: "Image error!",
                message: "Failed to crop image",
                color: "red",
                autoClose: 2000,
                icon: xIcon,
                withCloseButton: false
            });
        }
    };

    return (
        <Group>
            <input ref={fileInputRef} type="file" accept="image/*" style={{ display: "none" }} onChange={onFileChange} />
            <Box style={{ cursor: "pointer" }} onClick={onAvatarClick}>
                <Avatar src={pictureDataUrl} radius={60} size={120} />
                <Text size="xs" ta="center" c="dimmed">
                    Click avatar to change
                </Text>
            </Box>

            <Modal
                opened={cropModalOpen}
                onClose={() => setCropModalOpen(false)}
                size="md"
                withCloseButton={false}
                p="md"
            >
                <div style={{ position: "relative", width: "100%", height: 300, background: "#111" }}>
                    {selectedImage && (
                        <Cropper
                            image={selectedImage}
                            cropShape="round"
                            crop={crop}
                            zoom={zoom}
                            aspect={1}
                            onCropChange={setCrop}
                            onCropComplete={onCropComplete}
                            onZoomChange={setZoom}
                        />
                    )}
                </div>

                <Stack mt="md" align="stretch">
                    <Slider value={zoom} min={1} max={3} step={0.01} onChange={setZoom} w="50%" m="auto"/>
                    <Group justify="flex-end">
                        <Button variant="default" onClick={() => setCropModalOpen(false)}>
                            Cancel
                        </Button>
                        <Button onClick={handleApplyCrop}>Apply</Button>
                    </Group>
                </Stack>
            </Modal>
        </Group>
    );
}

export default EditableAvatar;