export function mimeDataUrlToBase64(dataUrl: string): string {
    return dataUrl.replace(/^data:.*;base64,/, "");
}

export function base64ToMimeDataUrl(bytes: string | undefined, mimeType = "image/png"): string | undefined {
    if (!bytes)
        return undefined;
    return `data:${mimeType};base64,${bytes}`;
}